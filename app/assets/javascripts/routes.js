var Routes = (function(routes) {
  var template = _.template($('#routes-form').html()),
      form,
      FORM_TRANSITION = 500
  ;
  
  function lastOfLine($this) {
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
  
  function preHideForm() {
    var $this = $(this);    
    
    hideForm(function(){
      $this.off('click.hide');
      $this.on('click.show', showForm);
    });
  };
  
  function hideForm(callback) {
    if(form !== undefined && form !== null) {
      setTimeout(function(){
        form.removeClass('expanded');
      }, 0);
      
      setTimeout(function(){
        form.remove();
        form = null;
        
        // Remove the arrow from the route
        $('.route').removeClass('selected');
        
        callback();
      }, FORM_TRANSITION);
    } else {
      callback();
    }
  };
  
  function showForm() {
    var $this = $(this);
    
    // Hide any previously displayed form
    hideForm(function(){
      // Fill the new form with the template
      form = $(template({}));

      // Find the last element of the line 
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
    });
  };
  
  routes.hook = function hook() {
    $('.route').on('click.show', showForm);
  };
  
  return routes;
  
}(routes || {}));