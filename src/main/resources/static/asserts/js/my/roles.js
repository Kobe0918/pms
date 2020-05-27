function getSettting() {
    var setting = {
        check : {
            enable : true,
            chkboxType : {
                "Y" : "ps",
                "N" : "ps"
            }
        },
        async : {
            enable : true,
        },
        data : {
            simpleData : {
                enable : true,
                idKey : "id",
                pIdKey : "pId",
                rootPId : 0
            }
        },
        callback : {
            onCheck : zTreeOnCheck
        }
    };

    return setting;
}


function zTreeOnCheck(event, treeId, treeNode) {
//	console.log(treeNode.id + ", " + treeNode.name + "," + treeNode.checked
//			+ "," + treeNode.pId);
//	console.log(JSON.stringify(treeNode));
}

function getMenuTree() {
    var root = {
        id : 0,
        name : "root",
        open : true,
    };

    $.ajax({
        type : 'get',
        url : '/permissions/selectAll',
        contentType : "application/json; charset=utf-8",
        async : false,
        success : function(data) {
            console.info(data + "::: data");
            var length = data.length;
            var children = [];
            for (var i = 0; i < length; i++) {
                var d = data[i];
                var node = createNode(d);
                children[i] = node;
            }
            root.children = children;
        }
    });
    return root;
}


function createNode(d) {
    var id = d['id'];
    var pId = d['parentId'];
    var name = d['name'];
    var child = d['child'];

    var node = {
        open : true,
        id : id,
        name : name,
        pId : pId,
    };

    if (child != null) {
        var length = child.length;
        if (length > 0) {
            var children = [];
            for (var i = 0; i < length; i++) {
                children[i] = createNode(child[i]);
            }
            node.children = children;
        }

    }
    return node;
}


function getCheckedMenuIds(){
    var permissionTable = $.fn.zTree.getZTreeObj("permissionTable");
    var nodes = permissionTable.getCheckedNodes(true);

    var length = nodes.length;
    var ids = [];
    for(var i=0; i<length; i++){
        var n = nodes[i];
        var id = n['id'];
        ids.push(id);
    }
    return ids;
}


function roleDetail(data){
    flag = true;
    $('#rolesModal').modal('show');
    $('#rolesModalLabel').text('修改角色信息');
    $('input[name="id"]').val(data.id);
    $('input[name="roleName"]').val(data.roleName);
    $('#roleStatus').val(data.roleStatus?'1':'0');
   $('#roleDescription').val(data.roleDescription);

   if(data.id !== ""){
       $.ajax({
           type : 'get',
           url : '/roles/'+data.id,
           async : false,
           success : function(data) {
               console.info(data  + " ::mm");
               initMenuChecked(data);
           }
       });
   }
    //  ids = data.ids;
    // console.info(ids);
    //
    //
    //
    // var s = $('#table input[name="btSelectItem"]');
    // $.each(s,function (index,item) {
    //     var val = parseInt($(this).val());
    //     console.info(val );
    //     if($.inArray(val,ids) !== -1){
    //         $(this).attr('checked',true);
    //     }
    // });
    //
    //
    //
    //
    // for(var i=0; i<length; i++){
    //     var node = treeObj.getNodeByParam("id", ids[i], null);
    //     treeObj.checkNode(node, true, false);
    // }
    //
    // if(ids.length === permissonData.length){
    //
    //     console.info('相等' );
    //      $('#table input[name="btSelectAll"]').attr('checked',true);
    // }else{
    //     console.info(ids.length );
    //     console.info(permissonData.length );
    // }
    // $table.bootstrapTable('load', permissonData);
}

function initMenuChecked(data) {
    var length = data.length;
    console.info(length + ":cd");
    var ids = [];
    for(var i=0; i<length; i++){
        ids.push(data[i]);
    }
    var treeObj = $.fn.zTree.getZTreeObj("permissionTable");
    var length = ids.length;
    if(length > 0){
        var node = treeObj.getNodeByParam("id", 0, null);
        treeObj.checkNode(node, true, false);
    }
    for(var i=0; i<length; i++){
        var node = treeObj.getNodeByParam("id", ids[i], null);
        treeObj.checkNode(node, true, false);
    }
}


$('#roleSaveBtn').click(function () {
    var bootstrapValidator = $("#roleForm").data('bootstrapValidator');
    bootstrapValidator.validate();
    if(!bootstrapValidator.isValid()){
        return;
    }


    //选中表格id
    var submitData = {};
    var postData = getCheckedMenuIds();
    submitData['permissionIds'] = postData;
    var data = $('#roleForm').serializeArray();
    $.each(data, function (index, item) {

            if ('roleStatus' === item.name) {
                submitData[item.name] = item.value === '1' ? 'true' : 'false';
            } else {
                submitData[item.name] = item.value;
            }
    });
    console.info(JSON.stringify(submitData));
    submit(JSON.stringify(submitData));
});

function submit(data) {
    $.ajax({
        method: "post",
        async: true,
        contentType: "application/json",
        url: flag?"/roles/editRole":"/roles/addRole",
        dataType: 'json',
        data: data,
        success: function (result) {
            if (result.success) {
                if(flag) {
                    toastr.success("修改成功", "提示：");
                }else{
                    toastr.success("添加成功","提示：");
                }
                setTimeout(function () {
                    $('#rolesModal').modal('hide');
                    $rolesTable.draw();
                    initModal();
                },800);
            }else{
                toastr.error(result.errorMsg,result.errorCode);
            }
        }
    });
}



//删除角色
$('#rolesDeleteBtn').click(function () {
    var ids = [];
    $(":checkbox").each(function (index, item) {
        if (item.checked && index !== 0) {
            ids.push($(this).val())
        }
    });
    console.info(JSON.stringify(ids));

    $('#delModalLabel').text("提示");
    $('#confirmSpan').text("确认删除选中角色，该操作不具有恢复性");
    $('#delConfirm').modal('show'); //手动打开模态框
    //防止按钮绑定事件后，重复之前的操作
    $('#delete-submit').off().on('click', function () {
        $.ajax({
            method: "delete",
            url: '/roles/deleteRoles',
            contentType: "application/json",
            data: JSON.stringify(ids),
            dataType: 'json',
            success: function (result) {
                if (result.success) {
                    toastr.success("操作" + result.errorMsg);
                    setTimeout(function () {
                        $rolesTable.draw(false);//查询后不需要保持分页状态，回首页
                    }, 800);
                } else {
                    toastr.error("错误信息", result.errorMsg);
                }
            },
            complete: function (response) {
                $('#delConfirm').modal('hide');
            }
        });
    })
});


$('#checkAll').on('click', function () {
    if ($('#checkAll').prop('checked') === true) {
        $('input[name="ids"]').prop('checked', 'checked');
    } else {
        $('input[name="ids"]').prop('checked', false);
    }
});


$('#roleQueryBtn').click(function () {
    $rolesTable.draw();
});

$('#resetBtn').click(function () {
    $('#roleName').val('');
    $rolesTable.draw();
    return false;
});


$('#addRoleBtn').click(function () {
    $('#rolesModal').modal('show');
    $('#rolesModalLabel').text('添加角色');
});

$('body').on('hidden.bs.modal', '.modal', function () {
    initModal();
});

function initModal(){
    flag = false;
    $('input[name="id"]').val('');
    var treeObj = $.fn.zTree.getZTreeObj("permissionTable");
    treeObj.checkAllNodes(false);
    $(':input', '#roleForm').not(':button, :submit, :reset, :checkbox').val('');
    $('#roleStatus').val('1');
}





