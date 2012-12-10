$(document).ready(function(){
  
  $('.route').on('click', function(){
    $(this).toggleClass('expanded');
  });

    // var displayError = function displayError(error) {
    //     var errors = {
    //         1: 'Permission denied',
    //         2: 'Position unavailable',
    //         3: 'Request timeout'
    //     };
    //     alert("Error: " + errors[error.code]);
    // };
    // 
    // var displayStops = function(position) {
    //     doRedirect(position.coords.latitude, position.coords.longitude);
    // };
    // 
    // var doRedirect = function(lat, lng) {
    //     console.log(lat, lng)
    //     window.location = '/nearby/' + lat + '/' + lng;
    // }
    // 
    // if(window._page === "locate") {
    //     var geocoder = new google.maps.Geocoder();
    // 
    //     $('#locate').on("click", function(){
    //         if (navigator.geolocation) {
    //             navigator.geolocation.getCurrentPosition(
    //                 displayStops,
    //                 displayError,
    //                 { enableHighAccuracy: true, timeout: 10 * 1000 * 1000, maximumAge: 0 }
    //             );
    //         } else {
    //             alert("Geolocation is not supported by this browser");
    //         }
    //     });
    // 
    //     $('#search-button').on("click", function(e){
    //         e.preventDefault();
    //         if (geocoder) {
    //             geocoder.geocode({ 'address': $("#address").val() }, function (results, status) {
    //                 if (status == google.maps.GeocoderStatus.OK) {
    //                     var lat = results[0].geometry.location.lat();
    //                     var lng = results[0].geometry.location.lng();
    // 
    //                     doRedirect(lat, lng);
    //                 } else {
    //                     $("#address").val('Unable to obtain coordinates for specified address')
    //                 }
    //             });
    //         }
    //     })
    // }
});