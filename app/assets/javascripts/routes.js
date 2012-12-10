var Routes = (function(routes) {
  var template = _.template($('#routes-form').html()),
      schedule_tpl = _.template($('#route-schedule').html()),
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
      
      var $radio = form.find('[name="direction"]');
      var $select = $radio.nextAll('select[name="stop"]');
      var $go = $radio.nextAll('input[type="submit"]');

      $radio.on('click.direction', function(){
        if($radio.val() == 1) {
          $select.html($this.find('script.inbound').html());
        } else {
          $select.html($this.find('script.outbound').html());
        }
        
        $go.removeAttr('disabled');
      });
      
      form.find('input[type="submit"]').on('click', function(e){
        e.preventDefault();
        
        showSchedule(
          $this.attr('data-route-id'),
          $radio.val(),
          $select.val()
        );
      });

      setTimeout(function(){
        form.addClass('expanded');
      }, 0);
    });
  };
  
  function showSchedule(route_id, direction, stop_id) {
    console.log(route_id, direction, stop_id);
    $.ajax({
      type: 'GET',
      url: 'http://' + window.location.host + '/schedule/route/' + route_id + '/direction/' + direction + '/stop/' + stop_id,
      success: onReceiveSchedule,
      error: onFetchScheduleError
    });
  }
  
  function onReceiveSchedule(data){
    console.log('Success :', { times: JSON.parse(data) });
    form.append(schedule_tpl({ times: JSON.parse(data) }));
  }
  
  function onFetchScheduleError() {
    console.log('Error :', arguments);
  }
  
  routes.hook = function hook() {
    $('.route').on('click.show', showForm);
  };
  
  return routes;
  
}(routes || {}));