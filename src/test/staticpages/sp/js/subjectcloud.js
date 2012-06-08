$(function() {
    //get subject json array
    $.getJSON("sp/subjects-data.json", function(data) {
        var subjects = data;
        var max = 1; // start for max count
        var min = 10000000; // start for min count (arbitrarily high number)
        var total = 0;
        //var length = subjects.tags.length;
        // find the highest and the lowest frequeny counts
        $.each(data.tags, function(i, val) {
            if (parseInt(val.freq) > parseInt(max)) {
                max = val.freq;
            }
            if (parseInt(val.freq) < parseInt(min)) {
                min = val.freq;
            }
            // add up the total frequency count, used to calculate the average (mean) frequency
            total = parseInt(total) + parseInt(val.freq);
        });
        // frequency ranges:
        // max ---- maxmeanmid ---- mean ---- minmeanmid ---- mid
        // average freguency
        var mean = Math.round(total / 50);
        // frequency between max and mean
        var maxmeanmid = Math.round((max - mean) / 2);
        // frequency between min and mean
        var minmeanmid = Math.round((mean - min ) / 2);
        // default font size
        var fontSize = "1em";
        //create list for tag links
        $("<ul>").attr("id", "subjectList").appendTo("#subjectCloud");
        //create tags
        $.each(data.tags, function(i, val) {
            var count = val.freq;
            // check to see where the frequecy falls withing the frequency range and set font size accordingly
            if ((count <= max) && (count > maxmeanmid)) {
                fontSize = "3em";
            }
            else if ((count <= maxmeanmid) && (count > mean)) {
                fontSize = "2.25em";
            }
            else if ((count <= mean) && (count > minmeanmid)) {
                fontSize = "1.5em";
            }
            else if ((count >= min) && (count <= minmeanmid)) {
                fontSize = "1em";
            }
            //create item
            var li = $("<li>");
            //create link
            $("<a>").text(val.tag).attr({title:"See all items related to \"" + val.tag + "\"", href:"brief-doc.html?start=1&view=table&query=subject%3A" + val.tag}).appendTo(li);
            //set tag size
            li.children().css("fontSize", fontSize);
            //add to list
            li.appendTo("#subjectList");
        });
    });
});