initSider("/dashboard");

var $province = $("#province"), $city = $("#city"), $country = $("#country");


//修改 checkbox 和radio 样式代码
$('input[type=radio],input[type=checkbox]').iCheck({
    checkboxClass: 'icheckbox_square-green',   // 可以更改red换颜色
    radioClass: 'iradio_square-blue',
    increaseArea: '20%' // optional
});


//datepicker 初始化
var $birthdayDatepicker = $('#birthday').datepicker({
    locale: 'zh-cn',
    format: 'yyyy-mm-dd ',
    uiLibrary: 'bootstrap4',
    maxDate: function () {
        var date = new Date();
        date.setDate(date.getDate());
        return new Date(date.getFullYear(), date.getMonth(), date.getDate());
    },
    weekStartDay: 1
});


//distpicker
//地区赋值
var $workAddress = $("#workAddress").distpicker(
    {
        autoSelect: false,
        // province: '福建省',
        // city: '厦门市',
        // country: '思明区'
    }
);
//重置  // $workAddress.distpicker('reset', true);


$.get('/user/getMyMessage', function (res) {
    var data = res.data;
    console.info(data);
    $('#image').attr('src',data.imgUrl);
    $('input[name="imgUrl"]').val(data.imgUrl);
    $('input[name="id"]').val(data.id);
    $('input[name="userName"]').val(data.userName);
    $('input[name="telephone"]').val(data.telephone);
    $('input[name="birthday"]').val(data.birthday);
    $('input[name="idCard"]').val(data.idCard);
    $('input[name="lastLoginTime"]').val(data.lastLoginTime);
    $('input[name="createTime"]').val(data.createTime);
    $('input[name="deptName"]').val(data.deptName);
    $('input[name="deptId"]').val(data.deptId);
    $('input[name="positionName"]').val(data.positionName);
    $('input[name="positionId"]').val(data.positionId);
    $('input[name="email"]').val(data.email);
    $('input[name="liveAddress"]').val(data.liveAddress);
    $('input[name="workTime"]').val(data.workTime);
    //是否已婚
    if (data.isMarry === 0) {
        $('#noMa').iCheck('check');
    } else {
        $('#isMa').iCheck('check');
    }
    if (data.sex === 0) {
        $('#isWomen').iCheck('check');
    } else {
        $('#isMan').iCheck('check');
    }
    var address = [],add = [];
    address = data.workAddress.split(",");
    $.each(address,function(index,item){
        add.push(item);
    });
    $province.val(add[0]);
    $province.trigger("change");
    $city.val(add[1]);
    $city.trigger("change");
    $country.val(add[2]);
    $country.trigger("change");

    $birthdayDatepicker.value(data.birthday);
});


// 文件上传
var $file = $('#file');
var $img = $("#image");
$img.click(function () {
    $file.click();
    $file.on('change', function () {
        var file = this.files[0];
        var fileTyoe = file.type.split;
        var fileType = file.type.split("/")[0];
        if (fileType != "image") {
            toastr.error("上传的照片格式不支持！");
            return false;
        }
        var reader = new FileReader();
        reader.onload = function (ev) {
            $img.attr('src',reader.result) ;
        };
        reader.readAsDataURL(file);
    })
});

validate();

//校验数据
function validate() {
    $('#userForm').bootstrapValidator({
        message: 'This value is not valid',
        fields: {
            userName: {
                message: '昵称不建议不填写',
                validators: {
                    notEmpty: {
                        message: '昵称不建议不填写。'
                    },
                    stringLength: {
                        max: 15,
                        min: 2,
                        message: '建议您输入真实的姓名，方便联系。'
                    }
                }
            },
            telephone: {
                validators: {
                    notEmpty: {
                        message: '请填写联系方式'
                    },
                    regexp: {
                        regexp: /^1[34578]\d{9}$/,
                        message: '填入正确的手机号码'
                    }
                }
            },
            email: {
                validators: {
                    regexp: {
                        regexp: /^[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/,
                            // /^[a-zA-Z0-9_-]+/,
                        message: '请填写正确的邮箱'
                    },
                    stringLength: {
                        max: 30,
                        min: 2,
                        message: '请填写正确的邮箱地址。'
                    }
                }
            },
            birthday: {
                validators: {
                    regexp: {
                        regexp: /^[1-9]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$/,
                        message: '请填写正确的邮箱'
                    },
                    stringLength: {
                        min: 1,
                        max: 11,
                        message: '时间格式为yyyy-MM-dd。'
                    }
                }
            },
            idCard: {
                validators: {
                    regexp: {
                        regexp: /^([1-6][1-9]|50)\d{4}(18|19|20)\d{2}((0[1-9])|10|11|12)(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/,
                        message: '请输入正确的身份证号码格式'
                    },
                    stringLength: {
                        min: 18,
                        max: 18,
                        message: '请填写正确的身份证号码。'
                    }
                }
            }
        }
    })
}

$('#userEdit').click(function () {
    var form = $('#userForm').data("bootstrapValidator");
    form.validate();
    if (!form.isValid()) {
        return;
    }


    var data = $('#userForm').serializeArray();
    $.each(data,function (index,item) {
          if(item.name === 'birthday' ){
              var reg = new RegExp("-","g");
              var value = item.value;
              item.value = value.replace(reg,"/");
              console.info("data : " + item.value);
          }
    });

    $.ajaxFileUpload({
        url: '/user/updateUserMessage?token='+localStorage.getItem("token"),
        contentType: "application/json",
        method: 'post',
        fileElementId: 'file',
        dataType: 'json',
        data: data ,
        success: function (res) {
            if(res.success){
                toastr.success("信息保存成功");
                var $btn = $('#userEdit');
                $btn.attr('disabled',"disabled");
                $btn.addClass('btn btn-outline-success btn-lg btn-block');
            }else{
                toastr.info(res.errorMsg);
            }
        }
    });
});









