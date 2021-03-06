<?php
	include_once("maLibSQL.pdo.php");
	include_once("maLibUtils.php");
	$request_method = $_SERVER["REQUEST_METHOD"];


	function getActivites()
	{
		$query="SELECT * FROM activite";
		echo json_encode(parcoursRs(SQLSelect($query)), JSON_PRETTY_PRINT);
	}
	
	function getActivite($id=0)
	{
		$query = "SELECT * FROM activite";
		if($id != 0)
		{
			$query .= " WHERE id=".$id." LIMIT 1";
		}
		
		echo json_encode(parcoursRs(SQLSelect($query)), JSON_PRETTY_PRINT);
		
	}

	
	function AddActivite()
	{
		$idEspace = valider("idEspace");
		$idIndicateur = valider("idIndicateur");
		$valeur = valider("valeur");
		$date = valider("date");
		$query="INSERT INTO activite(valeur,idEspace,idIndicateur,date) VALUES('".$valeur."', '".$idEspace."', '".$idIndicateur."', '".$date."')";
		if( $idEspace != null && $valeur!=null  && $date!= null && $idIndicateur!= null){
		$success = SQLInsert($query);
		if($success > 0)
			echo "activite ajoutée";
		}
		else
			echo "erreur";
	}
	
	function updateActivite($id)
	{
		header('Content-Type: application/json');
		$_PUT =file_get_contents('php://input');
		$obj = json_decode($_PUT);
		$idEspace = proteger($obj->idEspace);
		$date = proteger($obj->date);
		$valeur =  proteger($obj->valeur);
		$idIndicateur =  proteger($obj->idIndicateur);
		if( $idEspace!= null && $date!= null && $valeur!= null && $idIndicateur!= null){
		$query="UPDATE activite SET idEspace='".$idEspace."',date= '".$date."',valeur= '".$valeur."',idIndicateur= '".$idIndicateur."' WHERE id=".$id;
		$success = SQLUpdate($query);
		if($success > 0)
			echo "activite mis a jour";
		}
		else{
		echo "Problèmes de parametres.";
		}
	}
	
	function deleteActivite($id)
	{
		$query = "DELETE FROM activite WHERE id=".$id.";";
		$success = SQLDelete($query);
		if($success > 0)
			echo "indicateur supprime";
	}
	
	switch($request_method)
	{
		case 'GET':
			if(!empty($_GET["id"]))
			{
				$id=intval($_GET["id"]);
				getActivite($id);
			}
			else
			{
				getActivites();
			}
			break;
		default:
			header("HTTP/1.0 405 Method Not Allowed");
			break;
			
		case 'POST':
			AddActivite();
			break;
			
		case 'PUT':
			$id = intval($_GET["id"]);
			updateActivite($id);
			break;
			
		case 'DELETE':
			$id = intval($_GET["id"]);
			deleteActivite($id);
			break;

	}
?>