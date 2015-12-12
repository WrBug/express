<?php
require_once('simple_html_dom.php');
class JWCInfoManager extends CI_Model {

    public function __construct()
    {
        $this->load->database();
		$this->load->model('Util');
    }
	private function getLT()
	{
		$html = new simple_html_dom();
		$html->load_file('http://cas.hdu.edu.cn/cas/login');
		$divs = $html->find('input[name=lt]');
		$lt=$divs[0]->attr;
		$html->clear();
		return $lt['value'];
	}
	public function login($username,$password){
			$row=$this->get_user_info($username);
			if($row==null){
				$d=$this->get_user_from_jwc($username,$password);
				if($d==null){
					return	$this->Util->format_return(0,0,'wrong password');
				}
				return	$this->Util->format_return(0,1,'no user',$d);
			}else{
				if($password==$row['password']){
					$data=array('username'=>$row['username'],'name'=>$row['name'],'integral'=>$row['integral'],'releaseCount'=>$row['releaseCount'],'receiveCount'=>$row['receiveCount']);
					return	$this->Util->format_return(1,1,'success',$data);
				}else{
					return	$this->Util->format_return(0,5,'wrong password');	
				}	
			}
	}
	public function register($username,$password,$name,$idcard){
		$sql = 'insert into user (username,password,name,idcard,integral,createTime) values ("'.$username.'","'.$password.'","'.$name.'","'.$idcard.'","10","'.time().'")';
		$this->db->query($sql);
		$data=array('username'=>$username,'name'=>$name,'integral'=>10);
		return	$this->Util->format_return(1,1,'create user',$data);
	}
	private function get_user_info($username){
		$query = $this->db->query('SELECT * FROM user where username="'.$username.'" ');
		$row = $query->row_array();
		return $row;
	}
	
	public function get_user_from_jwc($username,$password){
		$getData='?lt='.$this->getLT().'&encodedService=http%3a%2f%2fjxgl.hdu.edu.cn%2findex.aspx'.'&service=http://jxgl.hdu.edu.cn/index.aspx'
		.'&serviceName=null'.'&loginErrCnt=0'.'&username='.$username.'&password='.$password;
		$url='http://cas.hdu.edu.cn/cas/login';
		$html = new simple_html_dom();
		$html->load_file($url.$getData);
		$ret = $html->find('a',0)->href;
		$title=$html->title;
		
		if(strpos($html, 'http://jxgl.hdu.edu.cn/index.aspx?ticket=')!=0){
			$html->clear();
			return substr($ret,strlen('http://jxgl.hdu.edu.cn/index.aspx?ticket='));
		}
		return null;
		//print_r($ret);
	}
	
	public function get_user_info_from_jwc($username,$name,$idcard,$cookie){
		$url='http://jxgl.hdu.edu.cn/xs_main.aspx?xh='.$username;
		$html=$this->send_post($url,$cookie);
		if(strpos($html, $name)!=0){
			$xm=urlencode(mb_convert_encoding($name,'gb2312','utf-8' ));
			$url=substr($html,strpos($html, 'xsgrxx.aspx'));
			$url=substr($url,0,strpos($url, '" tar')-1);
			$gnmkdm=substr($url,strpos($url, 'gnmkdm=')+7);
			$url='http://jxgl.hdu.edu.cn/xsgrxx.aspx?xh='.$username.'&xm='.$xm.'&gnmkdm='.$gnmkdm;
			$html=$this->send_post($url,$cookie);
			if(strpos($html, $idcard)!=0){
				return $this->Util->format_return(1,1,'success');
			}else{
				return $this->Util->format_return(0,3,'wrong idcard');
			}
		}else{
			if(strlen($html)<200){
				return	$this->Util->format_return(0,4,'wrong cookie');	
			}
			return	$this->Util->format_return(0,2,'wrong name');	
		}
	}
	private function send_post($url, $cookie) {
 		//$postdata = http_build_query(array());  
		$options = array(  
			'http' => array(  
			'method' => 'GET',  
			'header' => array('Cookie:'.$cookie,'Referer:'.$url),
			//'content' => $postdata,  
			'timeout' => 15 * 60 // 超时时间（单位:s）  
    		)  
  		);  
		$context = stream_context_create($options);  
		$result = file_get_contents($url, false, $context);  
		$result =iconv("gb2312", "utf-8//IGNORE",$result);
		return $result;  
	} 
}