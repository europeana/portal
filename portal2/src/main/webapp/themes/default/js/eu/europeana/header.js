/**
 * header.js
 * 
 * @package eu.europeana
 * @author dan entous <contact@gmtpluosone.com>
 * @author andy maclean <andyjmaclean@gmail.com>
 * 
 * @created 2011-07-06 17:34 GMT +1
 * @version 2011-10-20 08:26 GMT +1
 */

js.utils.registerNamespace('eu.europeana.header');

eu.europeana.header = {

    init904 : function(){

        console.log('will init904');

        window.NOFLogging_ready = window.NOFLogging_ready || [];
        window.NOFLogging_ready.push(function(){

            var config = {
                api_url : 'http://analytics.904labs.com',
                project_key : 'c6k2l3csHzhHlRMIEEyVPCEERcSDSozQDf1IPk0rfhg',
                log_mouse_movements : false,
                log_mouse_clicks : false,
                post_events_queue_on_browser_close : true,
                log_browser_close : true,
                debug : false
            };
            NOFLogging.init(config, function(){

                console.log('ready for action');

                /*
                 * Wrapper function for NOFLogging.query
                 * 
                 * @newUrl - requested search page url that facet data can be
                 * extracted from
                 * 
                 */
                var queryNOF = function(newUrl, saved){

                    var purl = $.url(newUrl);
                    var queryParam = purl.param('query');
                    var qf         = purl.param('qf');

                    if(qf || queryParam){

                        var facets = purl.param('qf');
                        var rowsParam = purl.param('rows');

                        var data = {
                            "NTW" : (window.history.length == 1)
                        };

                        if(rowsParam){
                            data.rows = rowsParam;
                        }

                        if(saved){
                            data.saved_search = true;
                        }

                        if(facets){
                            facets = (facets instanceof Array) ? facets : [facets];

                            var fd = {};

                            for(var i = 0; i < facets.length; i++){

                                var fs = facets[i].replace(/http:\/\//g, '').split(':');
                                var fName = fs[0];
                                var fVal = fs.length > 1 ? fs[1] : '';

                                if(fVal.length == 0){
                                    fVal = fName;
                                    fName = 'refinements';
                                }
                                if(fName == 'RIGHTS'){
                                    fVal = 'http://' + fVal;
                                }
                                if(fd[fs[0]]){
                                    fd[fName].push(fVal);
                                }
                                else{
                                    fd[fName] = [fVal];
                                }
                            }
                            data.facets = fd;
                        }
                        NOFLogging.query(queryParam ? queryParam : '*:*', data);
                    }
                }

                /*
                 * NOF binding for all pages
                 * 
                 */
                $('#query-search').submit(function(e){
                    var urlExt = 'http://x.com/?query=' + encodeURIComponent($('#query-search #query-input').val());
                    queryNOF(urlExt);
                });

                if(eu.europeana.vars.page_name == 'myeuropeana/index'){

                    $('#language-settings form input[type="submit"]').click(function(){

                        var state = NOFLogging.getState();

                        state.portal_language           = eu.europeana.vars.NOF_languageItem ? eu.europeana.vars.NOF_languageItem : "en";
                        state.portal_translate_keywords = $('#keyword-languages input[type="checkbox"]:checked').map(function(i, ob){
                            return $(ob).val();
                        }).get();
                        NOFLogging.setState(state);
                    });
                    
                    $('#saved-searches a').click(function(e){
                        var href = $(this).attr('href');
                        queryNOF(href, true);
                    });
                }
                if(eu.europeana.vars.page_name == 'full-doc.html'){

                    var href    = null;
                    var evtName = null;
                    var rank    = null;
                    var from    = window.location.href.split('record/')[1].split('.html')[0];
                    
                    var logNav = function(){
                        if(rank != null){
                            var purlHref = $.url(rank);
                            rank = parseInt(purlHref.param('start'));
                        }
                        if(href != null && evtName != null){
                            NOFLogging.logEvent(evtName, {to:href, to_rank: rank, from: from});
                        }                        
                    }
                    
                    $('#navigation li:nth-child(1) a').click(function(){

                        var href     = $(this).attr('href');
                        var purlHref = $.url(href);
                        var start    = parseInt(purlHref.param('start') ? purlHref.param('start') : 1);
                        
                        start = Math.ceil(start / eu.europeana.vars.rows);
                        
                        NOFLogging.logEvent('return_to_results', {results_page : start, rows : parseInt(eu.europeana.vars.rows)});
                    });
                    
                    $('#navigation li a.pagination-previous').click(function(){
                        href = $(this).attr('href');
                        rank = $(this).attr('href');
                        href = href.split('record/')[1].split('.html')[0];
                        evtName = 'prev_result';
                        logNav();
                    });
                    
                    $('#navigation li a.pagination-next').click(function(){
                        href = $(this).attr('href');
                        rank = $(this).attr('href');
                        href = href.split('record/')[1].split('.html')[0];
                        evtName = 'next_result';
                        logNav();
                    });
                    
                }
                if(eu.europeana.vars.page_name == 'search.html'){

                    /*
                     * Log results for this page
                     */

                    var getObjectIds = function(){

                        var objectIds = [];
                        $('.thumb-frame>a').each(function(){

                            var url = $(this).attr('href');
                            url = /\/record\/([^;]+).html/.exec(url)[1]
                            objectIds.push(url);
                        });
                        return objectIds;
                    };

                    NOFLogging.queryResults(getObjectIds(), eu.europeana.vars.msg.result_count, $('.thumb-frame').length);

                    /*
                     * Extra NOF binding for search page
                     * 
                     */

                    $('.nav-next a').add('.nav-prev a').add('.nav-first a').add('.nav-last a').click(function(e){

                        var url = $(e.target).attr('href');
                        var start = $.url(url).param('start');

                        var cPage = Math.ceil(eu.europeana.vars.msg.start / eu.europeana.vars.rows);
                        var nPage = Math.ceil(start / eu.europeana.vars.rows);

                        NOFLogging.paginate(nPage, cPage);
                    });

                    $('.jump-to-page').submit(function(e){

                        var cPage = Math.ceil(eu.europeana.vars.msg.start / eu.europeana.vars.rows);
                        var nPage = $(e.target).find('#start-page').val();

                        if(nPage && typeof parseInt(nPage) == 'number'){
                            NOFLogging.paginate(parseInt(nPage), cPage);
                        }
                        else{
                            console.log('not a number = ' + nPage);
                        }
                    });

                    $('.thumb-frame').add('.thumb-frame + a').click(function(e){

                        var url         = $(e.target).closest('.li').find('a').attr('href');
                        var purlClicked = $.url(url);
                        var rank        = parseInt(purlClicked.param('start') ? purlClicked.param('start') : 1);

                        url = url.split('record/')[1].split('.html')[0];
                        
                        NOFLogging.logEvent('clicked_result', { url:url, rank: rank } );
                    });

                    $('#facets-actions li input[type=checkbox]').click(function(e){

                        var url = ($(e.target)[0].nodeName.toUpperCase() == 'LABEL' ? $(e.target).closest('a') : $(e.target).next('a')).attr('href');
                        queryNOF(url);

                        e.stopPropagation();
                    });

                    $('#search-filter a').add('#search-filter a span').click(function(e){

                        
                        var url = ($(e.target)[0].nodeName.toUpperCase() == 'SPAN' ? $(e.target).closest('a') : $(e.target)).attr('href');
                        queryNOF(url);

                        e.stopPropagation();
                    });

                    $('#refine-search-form').submit(function(e){

                        var urlExt = '&qf=' + encodeURIComponent($('#refine-search-form #newKeyword').val());
                        queryNOF(window.location.href + urlExt);

                    });

                }



                // initial state has english as default language and no translatable keywords
                
                var state = NOFLogging.getState();
                
                var doOnLogout = function(){
                    state.portal_language = 'en';
                    state.portal_translate_keywords = [];
                    NOFLogging.setState(state);
                }
                
                if(!eu.europeana.vars.NOF.user && state.user_id){
                    doOnLogout();
                    NOFLogging.userLogout();
                }
                if(!state.user_id){
                    doOnLogout();
                }
                
                // NOF login
                
                if(eu.europeana.vars.NOF.user){

                    if(!NOFLogging.getState().user_id){
                        var username    = eu.europeana.vars.NOF.username;
                        var state       = NOFLogging.getState();
                        state.portal_language = eu.europeana.vars.NOF.portal_language;
                        state.portal_translate_keywords = eu.europeana.vars.NOF.portal_translate_keywords ? eu.europeana.vars.NOF.portal_translate_keywords : [];
                        
                        NOFLogging.setState(state);
                        NOFLogging.userLogin(username);
                    }

                }
                else{
                    var state       = NOFLogging.getState();

                    if(typeof $.cookie('portalLanguage') != 'undefined'){
                        state.portal_language = $.cookie('portalLanguage');
                    }
                    
                    if(typeof $.cookie('keywordLanguagesApplied') != 'undefined' && $.cookie('keywordLanguagesApplied') == 'true'){
                        if(typeof $.cookie('keywordLanguages') != 'undefined'){
                            state.portal_translate_keywords = $.cookie('keywordLanguages').split('|')
                        }
                    }
                }


                $('a[href="logout.html"]').click(function(){
                    doOnLogout();
                    NOFLogging.userLogout();
                })

                
            }); // end NOF init


        });

        js.loader.loadScripts([{
            file : 'purl.js',
            path : eu.europeana.vars.branding + '/js/com/purl/',
            callback : function(){

                js.loader.loadScripts([{
                    file : 'noflogging-0.2.min.js',
                    path : 'http://analytics.904labs.com/static/jssdk/'
                }]);
            }
        }]);
    },  /* end NOF logging */
    
    init : function(){

        /* general utilities */

        /* indexOf (for arrays not strings) in IE 8 */
        if(!Array.prototype.indexOf){
            Array.prototype.indexOf = function(obj, start){

                for(var i = (start || 0), j = this.length; i < j; i++){
                    if(this[i] === obj){
                        return i;
                    }
                }
                return -1;
            };
        }

        $('.submit-cell').css("width", $('.submit-cell button').outerWidth(true) + "px");
        $('.menu-cell').css("width", $('#search-menu').outerWidth(true) + "px");
        $('.submit-cell button').css("border-left", "solid 1px #4C7ECF"); // do
        // this
        // after
        // the
        // resize
        // to
        // stop
        // 1px
        // gap
        // in
        // FF

        // this.initResponsiveUtility();
        this.addLanguageChangeHandler();
        // this.addRefineSearchClickHandler();
        this.addAjaxMethods();
        // this.addMenuFocusTriggers();
        this.setupResultSize();
        this.setupSearchMenu();
        this.setupLanguageMenu();

        this.addAutocompleteHandler();
        this.setupNewsletter();
        this.setupPinterestAnalytics();

        $('#query-search').bind('submit', this.handleSearchSubmit);

        // setup tabs
        this.setupTabbing();
        this.setDefaultFocus();

        if(js.debug = true){
            this.init904();
        }
    },

    setupTabbing : function(){

        var nextTabIndex = 1;

        function setTabIndex(selectorOrObject){

            var selected = typeof selectorOrObject == 'string' ? $(selectorOrObject) : selectorOrObject;

            if(selected.length == 1){
                selected.attr('tabIndex', nextTabIndex);
                nextTabIndex++;
            }
            else if(selected.length > 1){
                selected.each(function(i, ob){

                    $(ob).attr('tabIndex', nextTabIndex);
                    nextTabIndex++;
                });
            }
        }

        if(eu.europeana.vars.page_name == 'login.html'){
            setTabIndex('#j_username');
            setTabIndex('#j_password');
            setTabIndex('#login input[type=submit]');
            setTabIndex('#login a');
        }

        /* header */
        setTabIndex('#query-input');
        setTabIndex('.submit-cell.hide-cell-on-phones button');
        setTabIndex('#search-menu');
        setTabIndex('#search-menu a');
        setTabIndex('#logo a');
        setTabIndex('#header-strip white>a, #header-strip a.white');
        setTabIndex('#lang-menu');
        setTabIndex('#lang-menu .item.lang a');
        setTabIndex('#query-info .search-help');

        /* search */

        if(eu.europeana.vars.page_name == 'search.html'){

            setTabIndex('#search-filter a');

            $('#filter-search a.facet-section').each(function(i, ob){

                setTabIndex($(ob));
                if(i == 0){
                    setTabIndex($(ob).parent().next('form').find('input[type!="hidden"]'));
                }
                else{
                    setTabIndex($(ob).parent().next('ul').find('a'));
                }
            });

            setTabIndex('#cb-ugc');

            setTabIndex('#share-subscribe .icon-share');

            setTabIndex('.nav-top .eu-menu');
            setTabIndex('.nav-top .eu-menu .item a');

            setTabIndex('.nav-top .nav-first a');
            setTabIndex('.nav-top .nav-prev a');
            setTabIndex('.nav-top #start-page');
            setTabIndex('.nav-top .nav-next a');
            setTabIndex('.nav-top .nav-last a');

            setTabIndex('.thumb-frame');

            setTabIndex('.nav-bottom .eu-menu');
            setTabIndex('.nav-bottom .eu-menu .item a');
            setTabIndex('.nav-bottom .nav-first a');
            setTabIndex('.nav-bottom .nav-prev a');

            setTabIndex('.nav-bottom #start-page');
            setTabIndex('.nav-bottom .nav-next a');
            setTabIndex('.nav-bottom .nav-last a');

            $("#filter-search input[type=checkbox]").not('#cb-ugc').attr("tabindex", "-1");
            $("#items .li a").attr("tabindex", "-1");
        }

    },

    setCookie : function(val){

        document.cookie = "europeana_rows=" + val;
    },

    getCookie : function(){

        var cookies = document.cookie.split(";");
        var cookieVal = null;

        for(var i = 0; i < cookies.length; i++){
            var cookieName = cookies[i].substr(0, cookies[i].indexOf("="));
            if(cookieName == "europeana_rows"){
                cookieVal = cookies[i].substr(cookies[i].indexOf("=") + 1, cookies[i].length);
            }
        }
        return cookieVal;
    },

    setupResultSize : function(){

        var rowsField = $("#query-search input[name=rows]");

        // first check the parameter - this will override any cookie
        if(eu.europeana.vars.rows && eu.europeana.vars.rows != "null"){
            rowsField.val(eu.europeana.vars.rows);

        }
        else{
            if(eu.europeana.vars.page_name != "search.html"){

                // check for cookie
                var cookie = eu.europeana.header.getCookie();
                if(cookie){
                    rowsField.val(cookie);
                }
                else{
                    if(js.utils.phoneTest()){
                        rowsField.val("12");
                    }
                    else{
                        rowsField.val("24");
                    }
                }
            }
        }
    },

    setupLanguageMenu : function(){

        var menu = new EuMenu($("#lang-menu"), {
            "fn_item" : function(self){

                if(self.getActive() == 'treat-as-link'){
                    window.location.href = self.getActiveHref();
                }
                else{
                    if(window.location.href.indexOf('#') > -1){
                        $("#language-selector").attr('method', 'GET');
                    }
                    $("input[name=lang]").val(self.getActive());
                    $("#language-selector").submit();
                }
            }
        });
        menu.setActive("choose");
        menu.init();
    },

    setupSearchMenu : function(){

        if($("#search-menu").length == 0){ // terms and conditions have no
            // search fields
            return;
        }
        var menu = new EuMenu($("#search-menu"), {
            "fn_item" : function(self){

            },

            "fn_init" : function(self){

                var input = $('#query-input');
                var searchTerm = input.attr("valueForBackButton").replace("*:*", "");
                self.cmp.find(".item a").each(function(i, ob){

                    var searchType = $(ob).attr("class");

                    if(searchTerm.indexOf(searchType) == 0){
                        self.setLabel(searchType);
                        input.val(searchTerm.substr(searchType.length, searchTerm.length));
                        self.setActive(searchType);
                    }
                });
            },

            "fn_submit" : function(self){

                var active = self.cmp.find(".item.active a").attr("class");
                var input = $('#query-input');
                input.val((typeof active == "undefined" ? "" : active) + input.val());
            }

        });

        menu.init();

        eu.europeana.header.searchMenu = menu;

        /* menu close */

        $(document).click(function(){

            $('.eu-menu').removeClass("active");
        });

        $("#query-search").bind("submit", function(){

            if(eu.europeana.vars.page_name == 'myeuropeana/index'){

                // remove any "returnToFacets" fields if the search term has
                // changed

                if($('.return-to-facet').length){
                    if($('#query-input').val() != $('#query-input').attr('valueForBackButton')){
                        $('.return-to-facet').remove();
                    }
                }
            }

            menu.submit();
            return true;
        });
    },

    /**
     * js solution for tabbing through main menu
     */
    /*
     * addMenuFocusTriggers : function() {
     * 
     * $('#menu-main li ul li a')
     * 
     * .focusin(function() {
     * 
     * $(this).parent().parent() .css({ 'margin-top' : 0, 'opacity' : 1 });
     * 
     * $(this).parent().parent().prev().children().eq(0) .css({ 'color' :
     * '#fff', 'background-color' : '#000', 'background-position' : 'right
     * -189px' }); })
     * 
     * .focusout(function() {
     * 
     * $(this).parent().parent() .css({ 'margin-top' : -499, 'opacity' : 0 });
     * 
     * $(this).parent().parent().prev().children().eq(0) .css({ 'color' :
     * '#000', 'background-color' : '#fff', 'background-position' : 'right
     * -173px' });
     * 
     * }); },
     */

    /**
     * adds focus to the search textbox
     */
    setDefaultFocus : function(){

        $('#query-input').focus(function(){

            $("#query-full table tr:first-child .query-cell").addClass("glow");
        });

        $('#query-input').blur(function(){

            $("#query-full table tr:first-child .query-cell").removeClass("glow");
        });

        if(eu.europeana.vars.page_name == 'staticpage.html'){
            return;
        }

        var inputFocus = ['login.html', 'forgotPassword.html', 'register-success.html'].indexOf(eu.europeana.vars.page_name) >= 0 ? $('#j_username') : $('#query-input');

        if(eu.europeana.vars.page_name == 'search.html'){
            inputFocus = window;
        }

        else if(eu.europeana.vars.page_name == 'full-doc.html'){
            var navigationLinks = $('#navigation a');
            if(navigationLinks.length){
                inputFocus = navigationLinks[0];
            }
            else{
                inputFocus = window;
            }
        }

        inputFocus.focus();

    },

    addAutocompleteHandler : function(){

        $('#query-input, #qf').each(function(i, id){

            $(id).autocomplete({

                open : function(event, ui){

                    var oldLeft = $(".ui-autocomplete").offset().left;
                    var oldWidth = $(".ui-autocomplete").width();
                    var newLeft = oldLeft - parseInt($(id).parent().css('padding-left'));
                    var newWidth = oldWidth - parseInt($(id).parent().css('padding-left'));
                    $(".ui-autocomplete").css("left", newLeft + "px");
                    $(".ui-autocomplete").css("width", newWidth + "px");
                    $(".ui-autocomplete").css("z-index", 2);
                },

                minLength : 3,

                delay : 200,

                source : function(request, response){

                    var filter = eu.europeana.header.searchMenu.getActive();
                    if(filter){
                        filter = filter.replace(/:/g, '');
                        request.field = filter;
                    }

                    $.getJSON(eu.europeana.vars.homeUrl + 'suggestions.json', request, function(data){

                        // create array for response objects
                        var suggestions = [];

                        // process response
                        $.each(data.suggestions, function(i, val){

                            val.label = val.term;
                            val.frequency = '<span dir="ltr">' + val.frequency + '</span>';
                            suggestions.push(val);
                        });
                        response(suggestions);
                    });

                },

                select : function(event, ui){

                    switch(this.id){
                        case 'query-input':

                            if(completionClasses[ui.item.field]){
                                eu.europeana.header.searchMenu.setActive(completionClasses[ui.item.field]);
                            }
                            setTimeout(function(){

                                $('#query-search').submit();
                            }, 10);
                            break;

                        case 'qf':

                            setTimeout(function(){

                                $('#refine-search-form').submit();
                            }, 10);
                            break;
                    }

                }

            });

            // Formatting

            $.ui.autocomplete.prototype._renderItem = function(ul, item){

                if(!item.label){
                    item.label = item.term;
                }

                item.label = item.label.replace(new RegExp("(?![^&;]+;)(?!<[^<>]*)(" + $.ui.autocomplete.escapeRegex(this.term) + ")(?![^<>]*>)(?![^&;]+;)", "gi"), "<strong>$1</strong>");
                item.label += " (" + item.frequency + ")";
                item.label += '<span style="float:right">' + completionTranslations[item.field] + '</span>';

                return $("<li></li>").data("item.autocomplete", item).append("<a>" + item.label + "</a>").appendTo(ul);
            };
        });
    },

    addLanguageChangeHandler : function(){

        $('#lang').change(function(){

            $('#language-selector').submit();
        });
    },
    /*
     * addRefineSearchClickHandler : function() {
     * $('#refine-search').click(function(e) { e.preventDefault();
     * $('#refine-search-form').fadeIn(); $('#qf').focus(); });
     * 
     * $('#close-refine-search').click(function(e) { e.preventDefault();
     * $('#refine-search-form').fadeOut();
     * 
     * }); },
     */
    addAjaxMethods : function(){

        eu.europeana.ajax.methods = new eu.europeana.ajax();
        eu.europeana.ajax.methods.init();

    },

    handleSearchSubmit : function(e){

        var emptySearch = $('#query-input').val().length < 1 || (eu.europeana.header.searchMenu.getActive() == $('#query-input').val());
        if(emptySearch){

            e.preventDefault();
            $('#query-input').addClass('error-border');
            $('#additional-feedback').addClass('error').html(eu.europeana.vars.msg.search_error);
            $('#query-input').val("");
        }

    },

    setupNewsletter : function(){

        $("#newsletter-trigger").click(function(){

            $(".iframe-wrap").html('<iframe marginheight="0" ' + 'marginwidth="0" ' + 'frameborder="0" ' + 'src="' + window.newsletter.iframeUrl + '"/>' + '<div class="close"></div>');

            $(".iframe-wrap, .close").unbind("click").each(function(i, ob){

                $(ob).click(function(e){

                    if(e.target == ob){
                        $(".overlaid-content").css('visibility', 'hidden');
                    }
                });
            });

            $(".overlaid-content").css('visibility', 'visible');
        });

        $(window).add('.iframe-wrap').bind('keydown', function(e){

            var key = window.event ? e.keyCode : e.which;
            if(key == 27){
                if($(".overlaid-content").hasClass('fullscreen')){
                    eu.europeana.fulldoc.lightboxOb.exitFullscreen(true);
                    return;
                }
                $(".overlaid-content").css('visibility', 'hidden');
                $("#lightbox").remove(); // this is needed to stop ie8 making
                // a black screen following closing
                // of the lightbox.
            }
        });

    },

    setupPinterestAnalytics : function(){

        $('.icon-pinterest-2').click(function(){

            com.google.analytics.europeanaEventTrack("Pinterest Activity", "pinterest site");
        });
    }

};
