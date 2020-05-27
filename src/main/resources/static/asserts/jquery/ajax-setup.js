$.ajaxSetup({
    cache : false,
    headers : {
         "Authorization" : "Bearer "+localStorage.getItem("token")
    },

    error : function(xhr, textStatus, errorThrown) {
        var msg = xhr.responseText;
        var response = JSON.parse(msg);
        var code = response.errorCode;
        var message = response.errorMsg;
        console.info(code + " : " +message);
        setTimeout(function () {
            console.info(code + " : " +message);
        },8000);
        if (code === 3001) {
            //无权
            toastr.warning(message);
        } else if ( code === 2001) {
            //登入过期或者未登入
            toastr.error(code + ": " + message);
            localStorage.removeItem("token");
            location.href = '/login';
        } else if (xhr.status === 400) {
            toastr.error(code + ": " + message);
            // localStorage.removeItem("token");
            // location.href = '/login';
        } else if (xhr.status === 500) {
            toastr.error('系统错误：' + message);
        }
    }

});

//getNameAndDeptName
$.get('/user/getMyMessage',function (res) {
    var data = res.data;
    if(data.imgUrl !== null && data.imgUrl !== '' ){
        $('#v1_imgUrl').attr('src',data.imgUrl);
    }
    $('#v1_userName').text(data.userName);
    $('#v1_deptName').text(data.deptName)
});
// $(function () {
//     $('[data-toggle="tooltip"]').tooltip()
// });

//加载侧边栏
function initSider(currentUrl){
    $.ajax({
        url:"/permissions/current",
        method:"get",
        async:false,
        dataType: 'json',
        success:function(data){
            if(!$.isArray(data)){
                location.href='/login';
                return;
            }
            var menu = $("#topMenu");
            $.each(data, function(i,item){
                var li= $('<li></li>');
                var a = $("<a href='javascript:;'></a>");
                    a.addClass("nav-link");
                    if(item.css != null && item.css != ''){
                        a.append(item.css+ "    ")
                    }
                    if(item.name != null && item.name != ''){
                        a.append("  " + item.name)
                    }
                    if(item.href != null && item.href != ''){
                        if(currentUrl !== null && currentUrl === item.href){
                             a.addClass('active');
                        }
                        a.attr('href',item.href);
                    }else{
                        a.attr('href','#docCollapse'+i);
                        a.attr('data-toggle',"collapse");
                    }

                    li.addClass("nav-item");
                    li.append(a);
                    menu.append(li);
              setChild(li,item.child,i,currentUrl)
            });

        }
    });
}

function setChild(li,child,index,currentUrl){
    if(child !== null && child.length >0 ){
        var ul = $('<ul></ul>');
        ul.addClass("collapse");
        ul.attr("id","docCollapse"+index);
        $.each(child,function (index,item) {
            var lic = $('<li></li>');
            var a  =$('<a></a>');
            a.addClass("nav-link");
            lic.addClass("side_child");
            if(item.name != null && item.name != ''){
                a.append("  " + item.name)
            }
            if(item.href != null && item.href != ''){
                if(currentUrl !== null && currentUrl === item.href){
                    a.addClass('active');
                    ul.addClass('show');
                }
                a.attr('href',item.href);
            }
            lic.append(a);
            ul.append(lic);
            li.append(ul);
            setChild(li,item.child);
        })
    }
}



$('#forgot').on('click',function () {
    $('#forgotModalLabel').text("修改密码");
   $('#forgotModal').modal('show');
});



$('#post2').off().on('click', function () {
    var pattern = /^1[34578]\d{9}$/;
    if (pattern.test($(this).parent('span').prev('input').val())) {
        $.sendMessage($(this).parent('span').prev('input').val(), 'post2');
    } else {
        toastr.error("手机号码格式不正确！");
    }
});
$.sendMessage = function (phone, event) {
    $.get('/user/sendMessage', {'phone': phone}, function (response) {
        if (response.code == '4') {
            $.setTime($('#' + event));
        } else {
            toastr.warning(response.msg);
        }
    });
};

//60s倒计时实现逻辑
var countdown = 120;
$.setTime = function (obj) {
    if (countdown == 0) {
        obj.prop('disabled', false);
        obj.children('span').text('');
        countdown = 120;//60秒过后button上的文字初始化,计时器初始化;
        return;
    } else {
        obj.prop('disabled', true);
        obj.children('span').text("(" + countdown + "s)");
        countdown--;
    }
    setTimeout(function () {
        $.setTime(obj)
    }, 1000) //每1000毫秒执行一次
};

var isShow2 = true;
$('#registerLook2').on('click', function () {
    if (isShow2) {
        $('#registerLook2').removeClass('glyphicon glyphicon-eye-close');
        $('#registerLook2').addClass('glyphicon glyphicon-eye-open');
        $('#password2').attr('type', "text");
        isShow2 = false;
    } else {
        $('#registerLook2').removeClass('glyphicon glyphicon-eye-open');
        $('#registerLook2').addClass('glyphicon glyphicon-eye-close');
        $('#password2').attr('type', "password");
        isShow2 = true;
    }
});
var isShow3 = true;
$('#registerLook3').on('click', function () {
    if (isShow3) {
        $('#registerLook3').removeClass('glyphicon glyphicon-eye-close');
        $('#registerLook3').addClass('glyphicon glyphicon-eye-open');
        $('#password3').attr('type', "text");
        isShow3 = false;
    } else {
        $('#registerLook3').removeClass('glyphicon glyphicon-eye-open');
        $('#registerLook3').addClass('glyphicon glyphicon-eye-close');
        $('#password3').attr('type', "password");
        isShow3 = true;
    }
});

$('#forgot-submit').off().on('click', function (index, item) {
    var password = $('#password2').val();
    var checkPassword = $('#password3').val();
    if (password === checkPassword) {
        if (password.length > 7 && password.length < 16) {
            var pattern = /^1[34578]\d{9}$/;
            var phone = $('#for_phone').val();
            if (pattern.test(phone)) {
                var data =$('#forgotForm').serializeArray();
                    var returnData = {};
                    $.each(data, function (index, item) {
                            returnData[item.name] = item.value;
                    });
                $.ajax({
                    url: '/user/updatePassword',
                    dataType: 'json',
                    data: JSON.stringify(returnData),
                    async: true,
                    contentType: "application/json",
                    method: 'POST',
                    success: function (response) {
                        if (response.success) {
                            // localStorage.setItem("token", response.data);
                            // location.href = '/login';
                            toastr.info('修改成功');
                            $('#forgotModal').modal('hide');
                            $(':input', '#forgotForm').not(':button, :submit, :reset').val('').removeAttr('checked').removeAttr('selected');
                            countdown = 0;
                        } else {
                            console.info("sssss");
                            toastr.info(response.errorCode + " : " + response.errorMsg);
                        }
                        return false;
                    }
                });
            } else {
                toastr.info("输入的正确格式的手机号码");
                return false;
            }
        } else {
            toastr.info("输入密码长度在8-16个字符");
            return false;
        }
    } else {
        toastr.info('两次密码不同');
        return false;
    }
});



$('#logout').on('click',function () {
   $.get('/logout',function () {
       localStorage.removeItem("token");
       location.href='/login';
   }) ;
});
