<?php  

function send_post($url, $post_data) {  
  
  $postdata = http_build_query($post_data);  
  $options = array(  
    'http' => array(  
      'method' => 'POST',  
      'header' => 'Cookie:ASP.NET_SessionId=0wbteyiomepmx1v3orflyovr;route=acc407ccf2cd41551684a22ae27321f5',  
      'content' => $postdata,  
      'timeout' => 15 * 60 // 超时时间（单位:s）  
    )  
  );  
  $context = stream_context_create($options);  
  $result = file_get_contents($url, false, $context);  
  
  echo $result;  
}  
  
//使用方法  
$post_data = array(  
  'username' => 'stclair2201',  
  'password' => 'handan'  
);  
?>  