
<?php
////////////////////////////////////Get Data
$rawBody = file_get_contents("php://input");
$getData = json_decode($rawBody);
$order = $getData->request->intent->slots->Scene->value;
$response;

file_get_contents("http://api.thingspeak.com/update?api_key=PH9Y0ELYPC393R3Y&field1=OFF");

/////////////////////////////////////Json init
if($order == "light on"){
        file_get_contents("http://api.thingspeak.com/update?api_key=09NTO8QMWG48LM2M&field1=1");
        $response = "light on";
}
else if($order == "light off"){
        file_get_contents("http://api.thingspeak.com/update?api_key=09NTO8QMWG48LM2M&field1=0");
        $response = "light off";
}
else if($order == "fan off"){
         file_get_contents("http://api.thingspeak.com/update?api_key=I07Y6U9TP4JMYL3P&field1=0");
        $response = "fan off";
}
else if($order == "fan on"){
         file_get_contents("http://api.thingspeak.com/update?api_key=I07Y6U9TP4JMYL3P&field1=1");
        $response = "fan on";
}
else{
        $response = "OK, Activated";
}

$myObj->version = "1.0";
$myObj->response = array("outputSpeech"=>array("type"=>"PlainText","text"=>$response),
"shouldEndSession"=>true);
$myObj->sessionAttributes = array();

$myJSON = json_encode($myObj);
echo $myJSON;
?>
