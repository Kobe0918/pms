function initConfig(){
    $.ajax({
        type : 'get',
        url : '/config/get',
        contentType : "application/json; charset=utf-8",
        async : false,
        success : function(response) {
            var data = response.data;
            console.info(JSON.stringify(data));
            $attendance_begin_time.value(data.attendanceBeginTime);
            attendance_end_time.value(data.attendanceEndTime);
            $('#attendance_x1').val(data.attendanceX1);
            $('#attendance_x2').val(data.attendanceX2);
            $('#attendance_y1').val(data.attendanceY1);
            $('#attendance_y2').val(data.attendanceY2);
            $('#pms_config_id').val(data.id);
            console.info(data.leaveDeptName+":"+  data.caigoSpiuserName +":"+data.initPositionName)
            var $leave_dept_id =  $("#leave_dept_id");
            $leave_dept_id.append(new Option(data.leaveDeptName,data.leaveDeptId,  false, true));


            var $caigo_spiuser_id =  $("#caigo_spiuser_id");
            $caigo_spiuser_id.append(new Option(data.caigoSpiuserName,data.caigoSpiuserId,  false, true));


            var $init_position_id =  $("#init_position_id");
            $init_position_id.append(new Option(data.initPositionNmae, data.initPositionId, false, true));
            $caigo_spiuser_id.trigger("change");
            $leave_dept_id.trigger("change");
            $init_position_id.trigger("change");
        }
    });
}


$('#submitBtn').on('click',function () {
    var bootstrapValidator = $("#pmsconfig").data('bootstrapValidator');
    bootstrapValidator.validate();
    if(!bootstrapValidator.isValid()){
        return;
    }

    var formData =  $('#pmsconfig').serializeArray();
    var data = {};
     $.each(formData,function (index,item) {
         if (item.value !== "") {
             data[item.name] = item.value;
         }
     });

     editConfig(data);
});


function editConfig(data){
        $.ajax({
            method: "post",
            async: true,
            contentType: "application/json",
            url: "/config/editConfig",
            dataType: 'json',
            data: JSON.stringify(data),
            success: function (result) {
                if (result.success) {
                    $('#submitBtn').attr('disabled',true);
                    $('#submitBtn').addClass('btn btn-outline-success btn-lg btn-block');
                    $('#submitBtn').text('修改成功');
                    toastr.success(result.errorMsg,result.errorCode);
                }else{
                    toastr.error(result.errorMsg,result.errorCode);
                }
            }
        });
}