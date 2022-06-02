<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">

    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <link rel="stylesheet" type="text/css"
          href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css">
    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript"
            src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>
    <script type="text/javascript">

        $(function () {

            // 给这一类标签都加上日历
            $(".myDateClass").datetimepicker({
                language: 'zh-CN',
                format: 'yyyy-mm-dd',
                minView: 'month', // 可以选择的最小视图
                initialDate: new Date(),
                autoclose: true,  // 选择完是否自动关闭
                todayBtn: true, // 设置是否显示今天的
                clearBtn: true
            });

            // 给创建按钮添加事件
            $("#createActivityBtn").click(function () {
                // 做一些初始化工作，把窗口中的内容清空，重置表单内容
                $("#createActivityForm").get(0).reset();
                // 弹出窗口
                $("#createActivityModal").modal("show");
            });

            // 给保存按钮添加事件
            $("#saveCreateActivityBtn").click(function () {
                // 收集参数
                var owner = $("#create-marketActivityOwner").val();
                var name = $.trim($("#create-marketActivityName").val());
                var startDate = $("#create-startDate").val();
                var endDate = $("#create-endDate").val();
                var cost = $.trim($("#create-cost").val());
                var description = $.trim($("#create-describe").val());
                // 表单验证
                if (owner == "") {
                    alert("所有者不能为空！");
                    return;
                }
                if (name == "") {
                    alert("名称不能为空！");
                    return;
                }
                if (endDate != "" && startDate != "") {
                    // 直接使用字符串比较大小
                    if (endDate < startDate) {
                        alert("开始时间不能晚于结束时间！");
                        return;
                    }
                }
                /*
                    使用正则表达似乎来判断字符串满不满足对应的匹配模式[0-9]*
                 */
                var regExp = /^(([1-9]\d*)|0)$/;
                if (!regExp.test(cost)) {
                    alert("成本只能是非负的整数！");
                    return;
                }
                // 发送请求
                $.ajax({
                    url: 'workbench/activity/saveCreateActivity.do',
                    data: {
                        owner: owner,
                        name: name,
                        startDate: startDate,
                        endDate: endDate,
                        cost: cost,
                        description: description
                    },
                    type: 'post',
                    datatype: 'json',
                    success: function (data) {
                        if (data.code == "1") {
                            // 成功了
                            $("#createActivityModal").modal("hide");
                            // 还要做个刷新的代码，将页面刷新
                            queryActivityByConditionForPage(1, $("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
                        } else {
                            alert(data.message);
                            // 模态窗口不关闭
                            $("#createActivityModal").modal("show"); // 可以不写，但是有用的
                        }
                    }
                });
            });

            // 当整个页面加载完的时候，查询
            queryActivityByConditionForPage(1, 10);

            // 给查询按钮添加单击事件
            $("#queryBtn").click(function () {
                //查询所有符合条件数据的第一页以及所有符合条件数据的总条数;
                queryActivityByConditionForPage(1, $("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
            });

            // 全选活动按钮绑定事件
            $("#myCheckAll").click(function () {
                // 如果选中按钮，则所有按钮全选中
                if (this.checked) {
                    // 父子选择器
                    $("#tBody input[type='checkbox']").prop("checked", true);
                } else {
                    $("#tBody input[type='checkbox']").prop("checked", false);
                }
            });

            // 选择活动
            $("#tBody").on("click", "input[type='checkbox']", function () {
                // 如果列表中的全部checkbox都选中
                if ($("#tBody input[type='checkbox']:checked").size() == $("#tBody input[type='checkbox']").size()) {
                    $("#myCheckAll").prop("checked", true);
                } else {
                    $("#myCheckAll").prop("checked", false);
                }
            });

            // 给删除按钮添加单击事件
            $("#deleteActivityBtn").click(function () {
                // 获取所有被选中的checkbox
                var checkedIds = $("#tBody input[type='checkbox']:checked");

                if (checkedIds.size() == 0) {
                    alert("删除操作至少选择一条记录！");
                    return;
                }
                if (window.confirm("确定删除吗？")) {
                    var ids = "";
                    $.each(checkedIds, function () {
                        ids += "id=" + this.value + "&";
                    });
                    ids = ids.substr(0, ids.length - 1);
                    // 向后台发请求
                    $.ajax({
                        url: 'workbench/activity/deleteActivityByIds.do',
                        data: ids,
                        type: 'post',
                        datatype: 'json',
                        success: function (data) {
                            if (data.code == "1") {
                                queryActivityByConditionForPage(1, $("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
                            } else {
                                alert(data.message);
                            }
                        }
                    });
                }
            });

            $("#editActivityBtn").click(function (){
                // 收集参数
                // 获取列表中被选中的checkedBox
                var checkedIds = $("#tBody input[type='checkbox']:checked");
                if (checkedIds.size() == 0){
                    alert("请选择要修改的数据！");
                    return;
                }
                if (checkedIds.size() > 1){
                    alert("每次只能选择一条数据进行修改！")
                    return;
                }
                var id = checkedIds.val();
                $.ajax({
                    url:'workbench/activity/queryActivityById.do',
                    data:{
                        id:id
                    },
                    type:'post',
                    datatype:'json',
                    success:function (data){
                        // 信息填充
                        $("#edit-id").val(data.id);
                        $("#edit-marketActivityOwner").val(data.owner);
                        $("#edit-marketActivityName").val(data.name);
                        $("#edit-startTime").val(data.startDate);
                        $("#edit-endTime").val(data.endDate);
                        $("#edit-cost").val(data.cost);
                        $("#edit-description").val(data.description);
                        // 弹出模态窗口
                        $("#editActivityModal").modal("show");
                    }
                });

            });

            // 给更新按钮添加单击事件
            $("#updateAndSubmitActivityBtn").click(function (){
                var id = $("#edit-id").val();
                var owner = $("#edit-marketActivityOwner").val();
                var name = $.trim($("#edit-marketActivityName").val());
                var startDate = $("#edit-startTime").val();
                var endDate = $("#edit-endTime").val();
                var cost = $.trim($("#edit-cost").val());
                var description = $.trim($("#edit-description").val());
                // 表单验证
                if (owner == "") {
                    alert("所有者不能为空！");
                    return;
                }
                if (name == "") {
                    alert("名称不能为空！");
                    return;
                }
                if (endDate != "" && startDate != "") {
                    // 直接使用字符串比较大小
                    if (endDate < startDate) {
                        alert("开始时间不能晚于结束时间！");
                        return;
                    }
                }
                /*
                    使用正则表达似乎来判断字符串满不满足对应的匹配模式[0-9]*
                 */
                var regExp = /^(([1-9]\d*)|0)$/;
                if (!regExp.test(cost)) {
                    alert("成本只能是非负的整数！");
                    return;
                }
                $.ajax({
                    url:'workbench/activity/updateActivityById.do',
                    data:{
                        id:id,
                        owner:owner,
                        name:name,
                        startDate:startDate,
                        endDate:endDate,
                        cost:cost,
                        description:description
                    },
                    type:'post',
                    datatype:'json',
                    success:function (data) {
                        if (data.code == "1") {
                            // 关闭模态窗口
                            $("#editActivityModal").modal("hide");
                            // 执行查询，刷新页面
                            queryActivityByConditionForPage(1, $("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
                        } else {
                            // 提示信息
                            alert(data.message);
                            // 模态窗口不关闭
                            $("#editActivityModal").modal("show");
                        }
                    }
                });


            });

            // 给批量导出绑定事件
            $("#exportActivityAllBtn").click(function (){
                window.location = "workbench/activity/exportAllActivity.do";
            });

            // 给选择导出绑定事件
            $("#exportActivityXzBtn").click(function () {
                var checkedIds = $("#tBody input[type='checkbox']:checked");
                if (checkedIds.size() == 0){
                    alert("请选择要导出的数据！");
                    return;
                }
                var ids = "";
                $.each(checkedIds, function () {
                    ids += "id=" + this.value + "&";
                });
                // 如何发送带信息的同步请求
                window.location.href = "workbench/activity/exportActivityByIds.do" + "?" + ids;
            });

            // 给导入按钮添加单击事件
            $("#importActivityBtn").click(function (){
                // 收集参数，下面这种方法只能获取文件名，不能获取文件对象
                // $("#activityFile").val();
                // 可以根据文件名做一个表单验证
                var FileName = $("#activityFile").val();
                // js截取字符串
                var type = FileName.substr(FileName.lastIndexOf(".")).toLowerCase();
                if (type != ".xls"){
                    alert("请导入.xls格式的文件！");
                    return;
                }
                var actFile = $("#activityFile")[0].files[0];
                if (actFile.size > 5 * 1024 * 1024){
                    alert("所选的文件过大！请重新上传5M大小以内的数据！")
                }

                var myData = new FormData();
                myData.append("activityFile", actFile);
                // formdata.append("userName", "张三");
                // 发送异步请求
                $.ajax({
                    url:'workbench/activity/importActivity.do',
                    data: myData,
                    processData: false, // 设置ajax提交参数之前，是否把参数统一转成字符串（默认方法是true）
                    contentType: false, // 设置ajax向后台提交参数之前，是否按照urlencoded编码
                    type: 'post',
                    dataType:'json',
                    success:function(data){
                        if (data.code == "1"){
                            alert("导入成功！");
                            // 关闭模态窗口
                            $("#importActivityModal").modal("hide");
                            queryActivityByConditionForPage(1, $("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
                        } else {
                            alert(data.message);
                            $("#importActivityModal").modal("show");
                        }
                    }
                });



            });


        });


        // 查询的函数
        function queryActivityByConditionForPage(pageNo, pageSize) {
            //收集参数
            var name = $("#query-name").val();
            var owner = $("#query-owner").val();
            var startDate = $("#query-startDate").val();
            var endDate = $("#query-endDate").val();
            //var pageNo=1;
            //var pageSize=10;
            //发送请求
            $.ajax({
                url: 'workbench/activity/queryActivityByConditionForPage.do',
                data: {
                    name: name,
                    owner: owner,
                    startDate: startDate,
                    endDate: endDate,
                    pageNo: pageNo,
                    pageSize: pageSize
                },
                type: 'post',
                dataType: 'json',
                success: function (data) {
                    //显示总条数
                    //$("#totalRowsB").text(data.totalRows);
                    //显示市场活动的列表
                    //遍历activityList，拼接所有行数据
                    var htmlStr = "";
                    $.each(data.activityList, function (index, obj) {
                        htmlStr += "<tr class=\"active\">";
                        htmlStr += "<td><input type=\"checkbox\" value=\"" + obj.id + "\"/></td>";
                        htmlStr += "<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench/activity/queryActivityForDetailsById.do?id="+obj.id+"'\">" + obj.name + "</a></td>";
                        htmlStr += "<td>" + obj.owner + "</td>";
                        htmlStr += "<td>" + obj.startDate + "</td>";
                        htmlStr += "<td>" + obj.endDate + "</td>";
                        htmlStr += "</tr>";
                    });
                    $("#tBody").html(htmlStr);
                    // 取消全选按钮的选择
                    $("#myCheckAll").prop("checked", false);
                    //计算总页数
                    var totalPages = 1;
                    if (data.totalRows % pageSize == 0) {
                        totalPages = data.totalRows / pageSize;
                    } else {
                        totalPages = parseInt(data.totalRows / pageSize) + 1;
                    }

                    //对容器调用bs_pagination工具函数，显示翻页信息
                    $("#demo_pag1").bs_pagination({
                        currentPage: pageNo,//当前页号,相当于pageNo

                        rowsPerPage: pageSize,//每页显示条数,相当于pageSize
                        totalRows: data.totalRows,//总条数
                        totalPages: totalPages,  //总页数,必填参数.

                        visiblePageLinks: 5,//最多可以显示的卡片数

                        showGoToPage: true,//是否显示"跳转到"部分,默认true--显示
                        showRowsPerPage: true,//是否显示"每页显示条数"部分。默认true--显示
                        showRowsInfo: true,//是否显示记录的信息，默认true--显示

                        //用户每次切换页号，都自动触发本函数;
                        //每次返回切换页号之后的pageNo和pageSize
                        onChangePage: function (event, pageObj) { // returns page_num and rows_per_page after a link has clicked
                            //js代码
                            //alert(pageObj.currentPage);
                            //alert(pageObj.rowsPerPage);
                            queryActivityByConditionForPage(pageObj.currentPage, pageObj.rowsPerPage);
                        }
                    });
                }
            });
        }

    </script>
</head>
<body>

<!-- 创建市场活动的模态窗口 -->
<div class="modal fade" id="createActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
            </div>
            <div class="modal-body">

                <form id="createActivityForm" class="form-horizontal" role="form">

                    <div class="form-group">
                        <label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-marketActivityOwner">
                                <c:forEach items="${userList}" var="user">
                                    <option value="${user.id}">${user.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-marketActivityName">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-startDate" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control myDateClass" id="create-startDate" readonly="true">
                        </div>
                        <label for="create-endDate" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control myDateClass" id="create-endDate" readonly="true">
                        </div>
                    </div>
                    <div class="form-group">

                        <label for="create-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-cost">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="create-describe" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="create-describe"></textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="saveCreateActivityBtn">保存</button>
            </div>
        </div>
    </div>
</div>

<!-- 修改市场活动的模态窗口 -->
<div class="modal fade" id="editActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal" role="form">
                    <input type="hidden" id="edit-id">
                    <div class="form-group">
                        <label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-marketActivityOwner">
                                <c:forEach items="${userList}" var="user">
                                    <option value="${user.id}">${user.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-startTime" value="2020-10-10">
                        </div>
                        <label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-endTime" value="2020-10-20">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-cost" value="5,000">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-description" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="edit-description">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="updateAndSubmitActivityBtn">更新</button>
            </div>
        </div>
    </div>
</div>

<!-- 导入市场活动的模态窗口 -->
<div class="modal fade" id="importActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
            </div>
            <div class="modal-body" style="height: 350px;">
                <div style="position: relative;top: 20px; left: 50px;">
                    请选择要上传的文件：<small style="color: gray;">[仅支持.xls]</small>
                </div>
                <div style="position: relative;top: 40px; left: 50px;">
                    <input type="file" id="activityFile">
                </div>
                <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;">
                    <h3>重要提示</h3>
                    <ul>
                        <li>操作仅针对Excel，仅支持后缀名为XLS的文件。</li>
                        <li>给定文件的第一行将视为字段名。</li>
                        <li>请确认您的文件大小不超过5MB。</li>
                        <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                        <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                        <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                        <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                    </ul>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
            </div>
        </div>
    </div>
</div>


<div>
    <div style="position: relative; left: 10px; top: -10px;">
        <div class="page-header">
            <h3>市场活动列表</h3>
        </div>
    </div>
</div>
<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
    <div style="width: 100%; position: absolute;top: 5px; left: 10px;">

        <div class="btn-toolbar" role="toolbar" style="height: 80px;">
            <form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">名称</div>
                        <input class="form-control" type="text" id="query-name">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">所有者</div>
                        <input class="form-control" type="text" id="query-owner">
                    </div>
                </div>


                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">开始日期</div>
                        <input class="form-control" type="text" id="query-startDate"/>
                    </div>
                </div>
                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">结束日期</div>
                        <input class="form-control" type="text" id="query-endDate">
                    </div>
                </div>

                <button type="button" class="btn btn-default" id="queryBtn">查询</button>

            </form>
        </div>
        <div class="btn-toolbar" role="toolbar"
             style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
            <div class="btn-group" style="position: relative; top: 18%;">
                <button type="button" class="btn btn-primary" id="createActivityBtn"><span
                        class="glyphicon glyphicon-plus"></span> 创建
                </button>
                <button type="button" class="btn btn-default" id="editActivityBtn"><span
                        class="glyphicon glyphicon-pencil"></span> 修改
                </button>
                <button type="button" class="btn btn-danger" id="deleteActivityBtn"><span
                        class="glyphicon glyphicon-minus"></span> 删除
                </button>
            </div>
            <div class="btn-group" style="position: relative; top: 18%;">
                <button type="button" class="btn btn-default" data-toggle="modal" data-target="#importActivityModal">
                    <span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）
                </button>
                <button id="exportActivityAllBtn" type="button" class="btn btn-default" ><span
                        class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）
                </button>
                <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span
                        class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）
                </button>
            </div>
        </div>
        <div style="position: relative;top: 10px;">
            <table class="table table-hover">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td><input type="checkbox" id="myCheckAll"/></td>
                    <td>名称</td>
                    <td>所有者</td>
                    <td>开始日期</td>
                    <td>结束日期</td>
                </tr>
                </thead>
                <tbody id="tBody">

                </tbody>
            </table>
            <div id="demo_pag1"></div>
        </div>


    </div>

</div>
</body>
</html>