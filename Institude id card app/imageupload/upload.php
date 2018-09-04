<?php
require "DbConnect.php";
if($con)
{
	$title="fogg";
	$image=$_POST['image'];
	
	$upload_path="uploads/$title.jpg";
	
  
	$sql="INSERT INTO images(title,path) VALUES ('$title','$upload_path')";
	
	if(mysqli_query($con,$sql))
	{
		file_put_contents($upload_path,base64_decode($image));
		
		echo json_encode(array('response'=>"Image uploaded successfully..."));
	}
	else
	{
		echo json_encode(array('response'=>"Image upload Failed..."));
	}
	
	mysqli_close($con);
	
}

?>