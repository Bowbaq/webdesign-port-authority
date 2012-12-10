var Routes = (function(routes) {
  var template = _.template($('#routes-form').html()),
      form
  ;
  
  var lastOfLine = function lastOfLine($this) {
      var ref_top = $this.position().top,
          prev = $this,
          next = $this.next('.route')
      ;
      
      while(next.position() && next.position().top <= ref_top) {
        prev = next;
        next = next.next('.route');
      }
      
      return prev;
  };
  
  var preHideForm = function preHideForm() {
    var $this = $(this);    
    
    if(form !== undefined && form !== null) {
      setTimeout(function(){
        form.removeClass('expanded');
      }, 0);
      
      setTimeout(function(){
        hideForm();
        $this.removeClass('selected');
      }, 500);
    }
    
    $this.off('click.hide');
    $this.on('click.show', showForm);
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
    lastOfLine($this).after(form);
    
    $this.addClass('selected');
    $this.off('click.show');
    $this.on('click.hide', preHideForm);
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
  
  routes.hook = function hook() {
    $('.route').on('click.show', showForm);
  };
  
  return routes;
  
}(routes || {}));