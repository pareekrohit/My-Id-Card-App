<?php

require "DbConnect.php";

if($con)
{
	
	
	$code=$_POST['code'];
	$name=$_POST['name'];
	$address=$_POST['address'];
	$logo=$_POST['logo'];
	$signature=$_POST['signature'];
	
	$upload_logo="logo/$name.jpg";
	$upload_signature="signature/$name.jpg";
  
	$sql="INSERT INTO intitude_details_tb(code,name_of_intitude,address,logo,signature) VALUES ('$code','$name','$address','$upload_logo','$upload_signature')";
	

	
	if(mysqli_query($con,$sql))
	{
		file_put_contents($upload_logo,base64_decode($logo));
		file_put_contents($upload_signature,base64_decode($signature));
		
		echo json_encode(array("response"=>"Data uploaded successfully..."));
	}
	else
	{
		echo json_encode(array("response"=>"SORRY! upload Failed..."));
	}
	
	mysqli_close($con);
	
}

?>