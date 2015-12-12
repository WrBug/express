<?php
class Util extends CI_Model {

    public function __construct()
    {
        //$this->load->database();
    }
	public function format_return($status,$code,$message,$data='')
	{
		$data=array('status'=>$status,'code'=>$code,'msg'=>$message,'data'=>$data);
		return $data;
	}
}