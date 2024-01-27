<?php


if(isset($_POST['jsonArray'])){
	
	$data = $_POST['jsonArray'];
    $jsonArrayInvs = json_decode($data, true);
	
	
	
	 foreach ($jsonArrayInvs as $jsonObjectInv) {
		
        echo "Inventario:\n";
        
       
        foreach ($jsonObjectInv as $key => $value) {
            echo $key . ": " . $value . "\n";
        }
        
  
        $jsonArrayProds = $jsonObjectInv['productos'];
        
  
        foreach ($jsonArrayProds as $jsonObjectProd) {
            echo "Producto:\n";
            
 
            foreach ($jsonObjectProd as $key => $value) {
                echo $key . ": " . $value . "\n";
            }
        }
        

        $jsonArrayImg = $jsonObjectInv['imagenes'];
        

        foreach ($jsonArrayImg as $jsonObjectImg) {
            echo "Imagen:\n";
            

            foreach ($jsonObjectImg as $key => $value) {
                echo $key . ": " . $value . "\n";
            }
        }
    }
	
	
	$response = 'Datos recibidos correctamente';
    echo $response;
	
}else{
	
	$response = 'Error: Se esperaba una solicitud POST';
    echo $response;
	
}
?>

