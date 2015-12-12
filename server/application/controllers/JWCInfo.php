<?php
class JWCInfo extends CI_Controller {
    public function __construct()
    {
        parent::__construct();
        $this->load->model('JWCInfoManager');
    }

	public function login(){
		$username=$_REQUEST['username'];
		$password=$_REQUEST['password'];
		$data=$this->JWCInfoManager->login($username,$password);
		echo json_encode($data);
	}
	public function getInfo(){
		$cookie=$_SERVER['HTTP_COOKIE'];
		$username=$_REQUEST['username'];
		$name=$_REQUEST['name'];
		$idcard=$_REQUEST['idcard'];
		//echo $cookie;
		$data=$this->JWCInfoManager->get_user_info_from_jwc($username,$name,$idcard,$cookie);
		echo json_encode($data);
	}
	public function register(){
		$username=$_REQUEST['username'];
		$name=$_REQUEST['name'];
		$idcard=$_REQUEST['idcard'];
		$password=$_REQUEST['password'];
		$data=$this->JWCInfoManager->register($username,$password,$name,$idcard);
		echo json_encode($data);
	}
}