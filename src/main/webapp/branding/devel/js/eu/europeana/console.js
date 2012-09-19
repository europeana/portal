function consoleReady(){
  var panels = ['record', 'search'];
  jQuery('input[name=function]').click(function(){
    var selectedPanel = $(this).val();
    for (i in panels) {
      var id = '#' + panels[i] + '-panel';
      if (panels[i] == selectedPanel) {
        $(id).show();
      } else {
        $(id).hide();
      }
    }
  })
}
