var Routes = (function(routes) {
  var form, from_tpl = _.template($('#routes-form').html()),
      schedule_tpl = _.template($('#route-schedule').html()), 
      FORM_TRANSITION = 500,
      $search = $('#search');
  ;
  
  var regular = ["1", "2", "4", "6", "7", "8", "11", "12", "13", "14", "15", "16", "17", "18", "19L", "20", "21", "22", "24", "26", "27", "29", "31", "36", "38", "39", "40", "41", "43", "44", "48", "51", "51L", "52L", "53", "53L", "54", "56", "57", "58", "59", "60", "61A", "61B", "61C", "61D", "64", "65", "67", "68", "69", "71", "71A", "71B", "71C", "71D", "74", "75", "77", "78", "79", "81", "82", "83", "86", "87", "88", "89", "91", "93"];
  var flyer = ["28X", "G2", "G3", "G31", "O1", "O12", "O5", "P1", "P10", "P12", "P13", "P16", "P17", "P2", "P3", "P67", "P68", "P69", "P7", "P71", "P76", "P78", "Y1", "Y45", "Y46", "Y47", "Y49"];
  var special = ["BLLB", "BLSV", "DQI", "MI", "RED", "SL"];
  
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
      form = $(from_tpl({}));

      // Find the last element of the line 
      lastOfLine($this).after(form);

      $this.addClass('selected');
      $this.off('click.show');
      $this.on('click.hide', preHideForm);
      
      var $radio = form.find('[name="direction"]');
      var $select = $radio.nextAll('select[name="stop"]');
      var $go = $radio.nextAll('input[type="submit"]');

      $radio.on('click.direction', function(){
        if($(this).val() == 1) {
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
          form.find('[name="direction"]:checked').val(),
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
    form.append(schedule_tpl({ times: JSON.parse(data) }));
  }
  
  function onFetchScheduleError() {
    console.log('Error :', arguments);
  }
  
  function filterRoutes(filter) {
    hideForm(function(){
      var $routes = $('.route');
      $routes.removeClass('hidden');
      _.forEach($routes, function(route){
        var $route = $(route);
        var name = $route.attr('data-route-name'),
            long_name = $route.attr('data-route-long-name');
        if(!filter(name, long_name)) {
          $route.addClass('hidden');
        }
      });
    });
  }
  
  function filterRegular(name) {
    return _.contains(regular, name);
  }
  
  function filterFlyer(name) {
    return _.contains(flyer, name);
  }
  
  function filterSpecial(name) {
    return _.contains(special, name);
  }
  
  function filterSearch(name, long_name) {
    var search = $search.val();
    return _.contains(name, search) || _.contains(long_name, search);
  }
  
  routes.hook = function hook() {
    $('.route').on('click.show', showForm);
    
    $('#all').on('click', function(){
      filterRoutes(function(){ return true; });
    });
    $('#regular').on('click', function(){
      filterRoutes(filterRegular);
    });
    $('#flyer').on('click', function(){
      filterRoutes(filterFlyer);
    });
    $('#special').on('click', function(){
      filterRoutes(filterSpecial);
    });
    
    $('#search').keyup(function(){
      filterRoutes(filterSearch);
    });
    
    $('#clear').on('click', function(){
      $search.val('');
      filterRoutes(function(){ return true; });
    });
  };
  
  return routes;
  
}(routes || {}));