initSider("/menu");
var permissonData = {};
 var $table = $('#table');


$.ajax({
    method: "get",
    async: true,
    url: "/permissions/selectAllByTreeGrid",
    dataType: 'json',
    success: function (data) {
        permissonData = data.data;
console.info(permissonData)
$table.bootstrapTable({
    data: permissonData,
    idField: 'id',
    dataType: 'jsonp',
    columns: [
        // {
        //     field: 'check', checkbox: true,
        //     formatter: function (value, row, index) {
        //
        //         if (row.check == true) {
        //             // console.log(row.serverName);
        //             //设置选中
        //             return {  checked: true };
        //         }
        //
        //     }
        // },

        // {field: 'id', title: '编号', sortable: true, align: 'center'},
        // {field: 'pid', title: '所属上级'},
        // { field: 'type',  title: '状态',  align: 'center',  formatter: 'statusFormatter'},
        { field: 'css', title: '样式' , align: 'center'},
        {
            field: 'name', title: '权限名称', align: 'center',formatter: function (value, row, index) {
                return row.name;
            }
        },
        { field: 'href', title: '路径', align: 'center' },

        { field: 'permission', title: '权限值', align: 'center',},
    ],
    // bootstrap-table-treegrid.js 插件配置 -- start

    //在哪一列展开树形
    treeShowField: 'name',
    //指定父id列
    parentIdField: 'parentId',

    onResetView: function (data) {
        //console.log('load');
        $table.treegrid({
              initialState: 'collapsed',// 所有节点都折叠
            //  initialState: 'expanded',// 所有节点都展开，默认展开
            treeColumn: 1,
             // expanderExpandedClass: 'glyphicon glyphicon-minus',  //图标样式
             // expanderCollapsedClass: 'glyphicon glyphicon-plus',
            onChange: function () {
                $table.bootstrapTable('resetWidth');
            }
        });

        //只展开树形的第一级节点
        $table.treegrid('getRootNodes').treegrid('expand');

    },
    onCheck: function (row) {
        var datas = $table.bootstrapTable('getData');

        // 勾选子类
        selectChilds(datas, row, "id", "parentId", true);

        // 勾选父类
        selectParentChecked(datas, row, "id", "parentId");

        // 刷新数据
         $table.bootstrapTable('load', datas);
    },
    onUncheck: function (row) {
        var datas = $table.bootstrapTable('getData');
        selectChilds(datas, row, "id", "parentId", false);
         $table.bootstrapTable('load', datas);
    }
    // bootstrap-table-treetreegrid.js 插件配置 -- end
});
    }
});

// 格式化状态
function statusFormatter(value, row, index) {
    console.info(value);
    if (value) {
        return '<span class="label label-success">二级目录</span>';
    } else {
        return '<span class="label label-default">一级目录</span>';
    }
}

/**
 * 选中父项时，同时选中子项
 * @param datas 所有的数据
 * @param row 当前数据
 * @param id id 字段名
 * @param pid 父id字段名
 */
function selectChilds(datas, row, id, pid, checked) {

    for (var i in datas) {
        if (datas[i][pid] == row[id]) {
            datas[i].check = checked;
            selectChilds(datas, datas[i], id, pid, checked);
        };
    }
}
function selectParentChecked(datas, row, id, pid) {
    for (var i in datas) {
        if (datas[i][id] == row[pid]) {
            datas[i].check = true;
            selectParentChecked(datas, datas[i], id, pid);
        }
    }
}
var selRows = $table.bootstrapTable("getSelections");

