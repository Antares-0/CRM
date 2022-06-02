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

            // 日历组件
            $(".myDateClass").datetimepicker({
                language: 'zh-CN',
                format: 'yyyy-mm-dd',
                minView: 'month', // 可以选择的最小视图
                initialDate: new Date(),
                autoclose: true,  // 选择完是否自动关闭
                todayBtn: true, // 设置是否显示今天的
                clearBtn: true
            });

            $("#isCreateTransaction").click(function () {
                if (this.checked) {
                    $("#create-transaction2").show(200);
                } else {
                    $("#create-transaction2").hide(200);
                }
            });

            // 搜索按钮单击事件
            $("#searchActivityBtn").click(function () {
                // 清空搜索框
                $("#searchActivityTxt").val("");
                // 清空搜索列表
                $("#tBody").html("");
                // 模态窗口
                $("#searchActivityModal").modal("show");
            });

            // 键盘弹起事件
            $("#searchActivityTxt").keyup(function () {
                var activityName = this.value;
                var clueId = '${clue.id}';
                $.ajax({
                    url: 'workbench/clue/queryActivityForConvertByNameClueId.do',
                    data: {
                        activityName: activityName,
                        clueId: clueId
                    },
                    type: 'post',
                    datatype: 'json',
                    success: function (data) {
                        var htmlStr = "";
                        $.each(data, function (index, obj) {
                            htmlStr += "<tr>";
                            htmlStr += "<td><input type=\"radio\" name=\"activity\" value=\"" + obj.id + "\" activityName=\"" + obj.name + "\"  /></td>";
                            htmlStr += "<td>" + obj.name + "</td>";
                            htmlStr += "<td>" + obj.startDate + "</td>";
                            htmlStr += "<td>" + obj.endDate + "</td>";
                            htmlStr += "<td>" + obj.owner + "</td>";
                            htmlStr += "</tr>";
                        });
                        $("#tBody").html(htmlStr);
                    }
                });
            });


            // 给动态的选择按钮添加事件
            $("#tBody").on("click", "input[type='radio']", function () {
                var id = this.value;
                var activityName = $(this).attr("activityName");
                $("#activityId").val(id);
                $("#activityName").val(activityName);
                // 关闭搜索模态窗口
                $("#searchActivityModal").modal("hide");

            });

            // 给转换按钮添加事件
            $("#convertBtn").click(function (){
                var isCreateTran = $("#isCreateTransaction").prop("checked");
                var clueId = '${clue.id}';
                var amountOfMoney = $.trim($("#amountOfMoney").val());
                var name = $.trim($("#tradeName").val());
                var expectedClosingDate = $("#expectedClosingDate").val();
                var stage = $("#stage").val();
                // var searchActivityBtn = $("#searchActivityBtn").val();
                var activityId = $("#activityId").val();
                // 表单验证
                if (amountOfMoney <= 0){
                    alert("金额不能为负数！");
                    return;
                }

                $.ajax({
                    url:'workbench/clue/convertClue.do',
                    data:{
                        isCreateTran:isCreateTran,
                        clueId:clueId,
                        money:amountOfMoney,
                        expectedDate:expectedClosingDate,
                        stage:stage,
                        activityId:activityId,
                        name:name
                    },
                    type:'post',
                    dataType:'json',
                    success:function (data){
                        if (data.code == "1"){
                            window.location.href='workbench/clue/index.do';
                        } else {
                            alert(data.message);
                        }
                    }
                });

            });


        });
    </script>

</head>
<body>

<!-- 搜索市场活动的模态窗口 -->
<div class="modal fade" id="searchActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 90%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title">搜索市场活动</h4>
            </div>
            <div class="modal-body">
                <div class="btn-group" style="position: relative; top: 18%; left: 8px;">
                    <form class="form-inline" role="form">
                        <div class="form-group has-feedback">

                            <input type="text" class="form-control" id="searchActivityTxt" style="width: 300px;"
                                   placeholder="请输入市场活动名称，支持模糊查询">
                            <span class="glyphicon glyphicon-search form-control-feedback"></span>
                        </div>
                    </form>
                </div>
                <table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
                    <thead>
                    <tr style="color: #B3B3B3;">
                        <td></td>
                        <td>名称</td>
                        <td>开始日期</td>
                        <td>结束日期</td>
                        <td>所有者</td>
                        <td></td>
                    </tr>
                    </thead>
                    <tbody id="tBody">
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<div id="title" class="page-header" style="position: relative; left: 20px;">
    <h4>转换线索 <small>${clue.fullname}${clue.appellation}-${clue.company}</small></h4>
</div>
<div id="create-customer" style="position: relative; left: 40px; height: 35px;">
    新建客户：${clue.company}
</div>
<div id="create-contact" style="position: relative; left: 40px; height: 35px;">
    新建联系人：${clue.fullname}${clue.appellation}
</div>
<div id="create-transaction1" style="position: relative; left: 40px; height: 35px; top: 25px;">
    <input type="checkbox" id="isCreateTransaction"/>
    为客户创建交易
</div>
<div id="create-transaction2"
     style="position: relative; left: 40px; top: 20px; width: 80%; background-color: #F7F7F7; display: none;">

    <form>
        <div class="form-group" style="width: 400px; position: relative; left: 20px;">
            <label for="amountOfMoney">金额</label>
            <input type="text" class="form-control" id="amountOfMoney">
        </div>
        <div class="form-group" style="width: 400px;position: relative; left: 20px;">
            <label for="tradeName">交易名称</label>
            <input type="text" class="form-control" id="tradeName" value="动力节点-">
        </div>
        <div class="form-group" style="width: 400px;position: relative; left: 20px;">
            <label for="expectedClosingDate">预计成交日期</label>
            <input type="text" class="form-control myDateClass" id="expectedClosingDate">
        </div>
        <div class="form-group" style="width: 400px;position: relative; left: 20px;">
            <label for="stage">阶段</label>
            <select id="stage" class="form-control">
                <option></option>
                <c:forEach items="${stageList}" var="stage">
                    <option value="${stage.id}">${stage.value}</option>
                </c:forEach>
            </select>
        </div>
        <div class="form-group" style="width: 400px;position: relative; left: 20px;">
            <label for="activityName">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" id="searchActivityBtn"
                                                      style="text-decoration: none;"><span
                    class="glyphicon glyphicon-search"></span></a></label>
            <input type="hidden" id="activityId">
            <input type="text" class="form-control" id="activityName" placeholder="点击上面搜索" readonly>
        </div>
    </form>

</div>

<div id="owner" style="position: relative; left: 40px; height: 35px; top: 50px;">
    记录的所有者：<br>
    <b>${clue.owner}</b>
</div>
<div id="operation" style="position: relative; left: 40px; height: 35px; top: 100px;">
    <input class="btn btn-primary" type="button" id="convertBtn" value="转换">
    &nbsp;&nbsp;&nbsp;&nbsp;
    <input class="btn btn-default" type="button" value="取消">
</div>
</body>
</html>