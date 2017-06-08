<?php
date_default_timezone_set("Asia/Seoul");
$url = "http://api.thingspeak.com/channels/247820/feeds/last.json?api_key=HC39Y2NQKXUV0HWW";
$json_data = json_decode(file_get_contents($url));

$updateDate = date("Y-m-d");
$updateHour = date("h:i:sa");

$temperature = floatval($json_data->field1);
if($temperature < 18){
    $temperature = array("Low", $temperature. "degrees");
}else if($temperature < 25){
    $temperature = array("Good", $temperature. "degrees");
}else{
    $temperature = array("High", $temperature. "degrees");
}


$humidity = floatval($json_data->field2);
if($humidity < 50){
    $humidity = array("Low", $humidity. "%");
}else if($humidity < 65){
    $humidity = array("Good", $humidity. "%");
}else{
    $humidity = array("High", $humidity. "%");
}

$gas = floatval($json_data->field3);
if($gas < 0.1){
    $gas = array("Good", $gas. "ppm");
}else{
    $gas = array("Danger", $gas. "ppm");
}

$dust = floatval($json_data->field4);
if($dust < 30){
    $dust = array("Good", $dust. "ppm");
}else if($dust < 80){
    $dust = array("Normal", $dust. "ppm");
}else{
    $dust = array("Bad", $dust. "ppm");
}
?>
