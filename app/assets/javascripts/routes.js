var Routes = (function(routes) {
  var template = _.template($('#routes-form').html()),
      form
  ;
  
  var firstOnNewLine = function firstOnNewLine($this) {
      var curr_top = $this.position().top;
      var next = $this.next();
      
      while(next.position().top <= curr_top) {
        next = next.next();
      }
      
      return next;
  };
  
  var preHideForm = function preHideForm() {
    var $this = $(this);
    
    if(form !== undefined && form !== null) {
      setTimeout(function(){
        form.removeClass('expanded');
      }, 0);
      
      setTimeout(hideForm, 500);
    }
    
    $this.one('click.show', showForm);
  };
  
  var hideForm = function hideForm() {
    if(form !== undefined && form !== null) {
        form.remove();
    }
  };
  
  var showForm = function showForm() {
    var $this = $(this);
    
    hideForm();
    form = $(template({}));
    firstOnNewLine($this).before(form);
    
    $this.one('click.hide', preHideForm);
    form.find('[name="direction"]').on('click.direction', function(){
      var $radio = $(this),
          $select = $radio.nextAll('select[name="stop"]'),
          $go = $radio.nextAll('input[type="submit"]')
      ;
      
      $select.html($this.find('script.inbound').html());
      $go.on('click', function(e){
        e.preventDefault();
        alert('This doesn\'t work yet ...');
      });
      $go.removeAttr('disabled');
    });
    
    setTimeout(function(){
      form.addClass('expanded');
    }, 0);
  };
  
  var fillStopSelect = function fillStopSelect() {

    
    
  };
  
  
  routes.hook = function hook() {
    $('.route').on('click.show', showForm);
  };
  
  return routes;
  
}(routes || {}));