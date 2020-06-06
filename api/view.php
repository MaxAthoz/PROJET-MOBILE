<?php
	include_once("maLibSQL.pdo.php");
	include_once("maLibUtils.php");
	$request_method = $_SERVER["REQUEST_METHOD"];


	function getActivites()
	{
		$query="SELECT * FROM myview";
		echo json_encode(parcoursRs(SQLSelect($query)), JSON_PRETTY_PRINT);
	}

	function getActivitesByUserAndId($idUser)
	{
		$query="SELECT * FROM myview WHERE idUser=".$idUser." ORDER by date DESC";

		$data["views"] = parcoursRs(SQLSelect($query));
        $data["success"] = true;
        $data["status"] = 201;
        echo json_encode($data, JSON_PRETTY_PRINT);
	}

   	function getActivitiesByUserAndDate($idUser,$date)
	{
		$query="SELECT * FROM myview WHERE idUser=".$idUser." AND date='".$date."'";

		$data["views"] = parcoursRs(SQLSelect($query));
        $data["success"] = true;
        $data["status"] = 201;
        echo json_encode($data, JSON_PRETTY_PRINT);
	}

	function deleteActivityOftheDay($idEspace,$date)
	{
		$query="DELETE FROM activite WHERE idEspace=".$idEspace." and date='".$date."'";

		$data["views"] = parcoursRs(SQLSelect($query));
        $data["success"] = true;
        $data["status"] = 201;
        echo json_encode($data, JSON_PRETTY_PRINT);
	}

	switch($request_method)
	{
		case 'GET':
			if(!empty($_GET["jour"]) && !empty($_GET["mois"]) && !empty($_GET["annee"]) && !empty($_GET["id"]))
			{
				$idUser=intval($_GET["id"]);
				$jour=intval($_GET["jour"]);
				$mois=intval($_GET["mois"]);
				$annee=intval($_GET["annee"]);
				$date="".$annee."-".$mois."-".$jour."";
				getActivitiesByUserAndDate($idUser,$date);
			}
			else if(!empty($_GET["idUser"]))
			{
				$idUser=intval($_GET["idUser"]);
				getActivitesByUserAndId($idUser);
			}
			else
			{
					getActivites();
			}
			break;
		case 'DELETE':
			if(!empty($_GET["jour"]) && !empty($_GET["mois"]) && !empty($_GET["annee"]) && !empty($_GET["id"]))
			{
				$idEspace=intval($_GET["id"]);
				$jour=intval($_GET["jour"]);
				$mois=intval($_GET["mois"]);
				$annee=intval($_GET["annee"]);
				$date="".$annee."-".$mois."-".$jour."";
				deleteActivityOftheDay($idEspace,$date);
			}
			break;
		default:
			header("HTTP/1.0 405 Method Not Allowed");
			break;


	}
?>