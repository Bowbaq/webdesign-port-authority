var Routes = (function(routes) {
  var template = _.template($('#routes-form').html()),
      form
  ;
  
  var firstOnNewLine = function firstOnNewLine(element) {
      var curr_top = element.position().top;
      var next = element.next();
      
      while(next.position().top <= curr_top) {
        next = next.next();
      }
      
      return next;
  };
  
  var preHideForm = function preHideForm() {
    var element = $(this);
    
    if(form !== undefined && form !== null) {
      setTimeout(function(){
        form.removeClass('expanded');
      }, 0);
      
      setTimeout(hideForm, 500);
    }
    
    element.off('click.hide');
    element.on('click.show', showForm);
  };
  
  var hideForm = function hideForm() {
    if(form !== undefined && form !== null) {
        form.remove();
    }
  };
  
  var showForm = function showForm() {
    var element = $(this);
    
    hideForm();
    form = $(template({}));
    firstOnNewLine(element).before(form);
    
    element.off('click.show');
    element.on('click.hide', preHideForm);
    
    setTimeout(function(){
      console.log("Adding expanded");
      form.addClass('expanded');
    }, 0);
  };
  
  
  routes.hook = function hook() {
    $('.route').on('click.show', showForm);
  };
  
  return routes;
  
}(routes || {}));