DROP TABLE IF EXISTS route_stops;
CREATE TABLE route_stops ( PRIMARY KEY (route_id, stop_id, route_direction) )
SELECT
    DISTINCT(route.id) AS route_id, 
    route.short_name AS route_short_name,
    route.long_name AS route_long_name,
    route.type AS route_type,
    trip.direction AS route_direction,
    stop.id AS stop_id, 
    stop.code AS stop_code,
    stop.name AS stop_name,
    stop.lat AS stop_lat,
    stop.lng AS stop_lng,
    stop.zone AS stop_zone
FROM route
    JOIN trip ON route.id = trip.route_id
    JOIN stop_time ON trip.id = stop_time.trip_id
    JOIN stop ON stop.id = stop_time.stop_id
ORDER BY trip.route_id ASC, trip.direction ASC;