$(document).ready(
    function(){
        //alert('ready');
        $('.TabCls').click(closeT);
        $('.bdy').click(showT);
    }
    ); 
onId=1;//当前打开的标签页
oldOnId=1;//当前之前打开的标签页

function toLogin(){//如果没有登陆，转到登录页面
    var isLogin=$("#isLogin").attr("value");
    if(isLogin!='true'){
        alert("you don't logon ,please input your e-mail address and your passwor to logon");
        showTN("t4");
        return false;
    }
    return true;
}


function showT(){
    try{       
        var i=$(this).parents("table").attr("id");
        if(i!='t4'&&i!='t1'){
            if(!toLogin()){
                return;
            }
        }
        var thisId=i.substring(1,i.length);
        if(thisId==onId)return;
        oldOnId=onId;
        onId=thisId;
        $('#t'+oldOnId).removeClass("on");
        $('#t'+onId).addClass("on");
        $('#t'+onId).show();
        $('#if'+oldOnId).hide();
        $('#if'+onId).show();
    }catch(e){
        alert(e);
    }
}

function showTN(name){
    try{
        var i=name;
        if(i!='t4'&&i!='t1'){
            if(!toLogin()){
                return;
            }
        }
        var thisId=i.substring(1,i.length);
        if(thisId==onId)return;
        oldOnId=onId;
        onId=thisId;
        $('#t'+oldOnId).removeClass("on");
        $('#t'+onId).addClass("on");
        $('#t'+onId).show();
        $('#if'+oldOnId).hide();
        $('#if'+onId).show();
    }catch(e){
        alert(e);
    }
}

function closeT(){
    try{
        var i=$(this).parents("table").attr("id");
        var thisId=i.substring(1,i.length);
        $('#t'+thisId).hide();
        if(thisId==onId){
            $('#if'+thisId).hide();
            $('#t'+oldOnId).show();
            $('#t'+oldOnId).addClass("on");
            $('#if'+oldOnId).show();
        }
        onId=oldOnId;
        oldOnId=1;
    }catch(e){
        alert(e);
    }

}

function logout(){
    
}
