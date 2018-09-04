<?php
 
        $con = mysqli_connect('localhost','root', '', 'institude_id_card');
 
        if ($con) {
            echo 'connected';
        }
		else{
			echo"not connected";
		}
	

?>