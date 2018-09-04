<?php
require "DbConnect.php";
if($con)
{
	
	$id=$_POST['id'];
	$image=$_POST['image'];
	$name=$_POST['name'];
	$clasess=$_POST['clasess'];
	$division=$_POST['division'];
	$dob=$_POST['dob'];
	$blood=$_POST['blood'];
	$mobile=$_POST['mobile'];
	$address=$_POST['address'];
	
	$upload_path="uploads/$name.jpg";
	
  
	$sql="INSERT INTO student_detail_tb(IID,picture,name,class,division,dob,blood_group,mobile,address) VALUES ('$id','$upload_path','$name','$clasess','$division','$dob','$blood','$mobile','$address')";
	

	
	if(mysqli_query($con,$sql))
	{
		file_put_contents($upload_path,base64_decode($image));
		//key and the value
		echo json_encode(array('response'=>"Data uploaded successfully"));
	}
	else
	{
		echo json_encode(array('response'=>"SORRY! upload Failed"));
	}
	
	mysqli_close($con);
	
}

?>