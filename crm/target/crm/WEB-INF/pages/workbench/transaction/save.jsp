<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>
    <script type="text/javascript">

        $(function () {

            // 阶段下拉框添加change
            $("#create-stage").change(function () {
                var stage = $(this).find("option:selected").text();
                if (stage == "") {
                    // 清空输入框
                    $("#create-possibility").val("");
                }
                $.ajax({
                    url: 'workbench/transaction/getPossibilityByStage.do',
                    data: {
                        stage: stage
                    },
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        $("#create-possibility").val(data);
                    }
                });
            });

            //当容器加载完成之后，对容器调用工具函数
            //当容器加载完成之后，对容器调用工具函数
            $("#create-customerName").typeahead({
                source:function (jquery,process) {//每次键盘弹起，都自动触发本函数；我们可以向后台送请求，查询客户表中所有的名称，把客户名称以[]字符串形式返回前台，赋值给source
                    //process：是个函数，能够将['xxx','xxxxx','xxxxxx',.....]字符串赋值给source，从而完成自动补全
                    //jquery：在容器中输入的关键字
                    //var customerName=$("#customerName").val();
                    //发送查询请求
                    $.ajax({
                        url:'workbench/transaction/queryCustomerNameByName.do',
                        data:{
                            customerName:jquery
                        },
                        type:'post',
                        dataType:'json',
                        success:function (data) { //['xxx','xxxxx','xxxxxx',.....]

                            process(data);
                        }
                    });
                }
            });


            // 加日历
            $(".myDateClass").datetimepicker({
                language: 'zh-CN',
                format: 'yyyy-mm-dd',
                minView: 'month', // 可以选择的最小视图
                initialDate: new Date(),
                autoclose: true,  // 选择完是否自动关闭
                todayBtn: true, // 设置是否显示今天的
                clearBtn: true
            });

            // 市场活动源
            $("#create-activitySrc").click(function () {
                // 清空
                $("#queryActivityForTransactionByName").val("");
                // 模态窗口显示
                $("#findMarketActivity").modal("show");
                // 清空
                $("#tBody").html("");
            });

            // 查找联系人
            $("#create-contactsName").click(function () {
                // 清空
                $("#queryContactTxt").val("");
                // 模态窗口显示
                $("#findContacts").modal("show");
                // 清空
                $("#tBodyEdit").html("");
            });

            // 动态查询活动列表
            $("#queryActivityForTransactionByName").keyup(function () {
                // 发一个异步请求,查一下全部的活动
                var name = $.trim($("#queryActivityForTransactionByName").val());
                $.ajax({
                    url: 'workbench/transaction/queryAllActivityForSave.do',
                    data: {
                        name: name
                    },
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        var htmlStr = "";
                        $.each(data, function (index, obj) {
                            htmlStr += "<tr>";
                            htmlStr += "<td><input type=\"radio\" name=\"activity\" value=\"" + obj.id + "\"/></td>";
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

            // 动态查询联系人
            $("#queryContactTxt").keyup(function () {
                var name = $.trim($("#queryContactTxt").val());
                $.ajax({
                    url: 'workbench/transaction/queryAllContactsForSave.do',
                    data: {
                        name: name
                    },
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        var htmlStr = "";
                        $.each(data, function (index, obj) {
                            htmlStr += "<tr>";
                            htmlStr += "<td><input type=\"radio\" name=\"activity\" value=\"" + obj.customerId + "\"/></td>";
                            htmlStr += "<td>" + obj.fullname + "</td>";
                            htmlStr += "<td>" + obj.email + "</td>";
                            htmlStr += "<td>" + obj.mphone + "</td>";
                            htmlStr += "</tr>";
                        });
                        $("#tBodyEdit").html(htmlStr);
                    }
                });
            });

            $("#tBody").on("click", "input[name='activity']:checked", function () {
                var activityId = $(this).val();
                // 数据回显
                $("#create-activitySrc").val(activityId);
            });

            $("#tBodyEdit").on("click", "input[name='activity']:checked", function () {
                var contactsId = $(this).val();
                // 数据回显
                $("#create-contactsName").val(contactsId);
            });


            $("#saveTransactionBtn").click(function () {
                var owner = $("#create-transactionOwner").val();
                var money = $("#create-amountOfMoney").val();
                var name = $("#create-transactionName").val();
                var expectedDate = $("#create-expectedClosingDate").val();
                var customerId = $("#create-accountName").val();
                var stage = $("#create-stage").val();
                var type = $("#create-transactionType").val();
                var source = $("#create-clueSource").val();
                var activityId = $("#create-activitySrc").val();
                var contactsId = $("#create-contactsName").val();
                var description = $("#create-describe").val();
                var contactSummary = $("#create-contactSummary").val();
                var nextContactTime = $("#create-nextContactTime").val();

                // 表单验证
                if (owner == "" || money == "" || expectedDate == "" || customerId == "" || stage == "") {
                    alert("所有字段都必须非空！");
                    return;
                }
                if (type == "" || source == "" || activityId == "" || contactsId == "" || description == "") {
                    alert("所有字段都必须非空！");
                    return;
                }
                if (contactSummary == "" || nextContactTime == "") {
                    alert("所有字段都必须非空！");
                    return;
                }

                alert(activityId);

                $.ajax({
                    url: 'workbench/transaction/saveCreateTransaction.do',
                    data: {
                        owner: owner,
                        money: money,
                        name: name,
                        expectedDate: expectedDate,
                        customerId: customerId,
                        stage: stage,
                        type: type,
                        source: source,
                        activityId: activityId,
                        contactsId: contactsId,
                        description: description,
                        contactSummary: contactSummary,
                        nextContactTime: nextContactTime,
                    },
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == "1") {
                            // 创建成功
                            window.location.href = 'workbench/transaction/index.do';
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
<!-- 查找市场活动模态窗口 -->
<div class="modal fade" id="findMarketActivity" role="dialog">
    <div class="modal-dialog" role="document" style="width: 80%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title">查找市场活动</h4>
            </div>
            <div class="modal-body">
                <div class="btn-group" style="position: relative; top: 18%; left: 8px;">
                    <form class="form-inline" role="form">
                        <div class="form-group has-feedback">
                            <input type="text" class="form-control" style="width: 300px;"
                                   placeholder="请输入市场活动名称，支持模糊查询" id="queryActivityForTransactionByName">
                            <span class="glyphicon glyphicon-search form-control-feedback"></span>
                        </div>
                    </form>
                </div>
                <table id="activityTable3" class="table table-hover"
                       style="width: 900px; position: relative;top: 10px;">
                    <thead>
                    <tr style="color: #B3B3B3;">
                        <td></td>
                        <td>名称</td>
                        <td>开始日期</td>
                        <td>结束日期</td>
                        <td>所有者</td>
                    </tr>
                    </thead>
                    <tbody id="tBody">


                    <%--<tr>
                        <td><input type="radio" name="activity"/></td>
                        <td>发传单</td>
                        <td>2020-10-10</td>
                        <td>2020-10-20</td>
                        <td>zhangsan</td>
                    </tr>--%>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<!-- 查找联系人 -->
<div class="modal fade" id="findContacts" role="dialog">
    <div class="modal-dialog" role="document" style="width: 80%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title">查找联系人</h4>
            </div>
            <div class="modal-body">
                <div class="btn-group" style="position: relative; top: 18%; left: 8px;">
                    <form class="form-inline" role="form">
                        <div class="form-group has-feedback">
                            <input type="text" class="form-control" style="width: 300px;" placeholder="请输入联系人名称，支持模糊查询"
                                   id="queryContactTxt">
                            <span class="glyphicon glyphicon-search form-control-feedback"></span>
                        </div>
                    </form>
                </div>
                <table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
                    <thead>
                    <tr style="color: #B3B3B3;">
                        <td></td>
                        <td>名称</td>
                        <td>邮箱</td>
                        <td>手机</td>
                    </tr>
                    </thead>
                    <tbody id="tBodyEdit">
                    <%--<tr>
                        <td><input type="radio" name="activity"/></td>
                        <td>李四</td>
                        <td>lisi@bjpowernode.com</td>
                        <td>12345678901</td>
                    </tr>--%>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>


<div style="position:  relative; left: 30px;">
    <h3>创建交易</h3>
    <div style="position: relative; top: -40px; left: 70%;">
        <button type="button" class="btn btn-primary" id="saveTransactionBtn">保存</button>
        <button type="button" class="btn btn-default">取消</button>
    </div>
    <hr style="position: relative; top: -40px;">
</div>
<form class="form-horizontal" role="form" style="position: relative; top: -30px;">
    <div class="form-group">
        <label for="create-transactionOwner" class="col-sm-2 control-label">所有者<span
                style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="create-transactionOwner">
                <c:forEach items="${userList}" var="user">
                    <option value="${user.id}">${user.name}</option>
                </c:forEach>
            </select>
        </div>
        <label for="create-amountOfMoney" class="col-sm-2 control-label">金额</label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="create-amountOfMoney">
        </div>
    </div>

    <div class="form-group">
        <label for="create-transactionName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="create-transactionName">
        </div>
        <label for="create-expectedClosingDate" class="col-sm-2 control-label">预计成交日期<span
                style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control myDateClass" id="create-expectedClosingDate">
        </div>
    </div>

    <div class="form-group">
        <label for="create-customerName" class="col-sm-2 control-label">客户名称<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="create-customerName" placeholder="支持自动补全，输入客户不存在则新建">
        </div>
        <label for="create-stage" class="col-sm-2 control-label">阶段<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="create-stage">
                <option></option>
                <c:forEach items="${stageList}" var="s">
                    <option value="${s.id}">${s.value}</option>
                </c:forEach>
            </select>
        </div>
    </div>

    <div class="form-group">
        <label for="create-transactionType" class="col-sm-2 control-label">类型</label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="create-transactionType">
                <option></option>
                <c:forEach items="${transactionTypeList}" var="tra">
                    <option value="${tra.id}">${tra.value}</option>
                </c:forEach>
            </select>
        </div>
        <label for="create-possibility" class="col-sm-2 control-label">可能性</label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="create-possibility">
        </div>
    </div>

    <div class="form-group">
        <label for="create-clueSource" class="col-sm-2 control-label">来源</label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="create-clueSource">
                <option></option>
                <c:forEach items="${sourceList}" var="source">
                    <option value="${source.id}">${source.value}</option>
                </c:forEach>
            </select>
        </div>
        <label for="create-activitySrc" class="col-sm-2 control-label">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);"
                                                                                           data-toggle="modal"
                                                                                           data-target="#findMarketActivity"><span
                class="glyphicon glyphicon-search"></span></a></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="create-activitySrc">
        </div>
    </div>

    <div class="form-group">
        <label for="create-contactsName" class="col-sm-2 control-label">联系人名称&nbsp;&nbsp;<a href="javascript:void(0);"
                                                                                            data-toggle="modal"
                                                                                            data-target="#findContacts"><span
                class="glyphicon glyphicon-search"></span></a></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="create-contactsName">
        </div>
    </div>

    <div class="form-group">
        <label for="create-describe" class="col-sm-2 control-label">描述</label>
        <div class="col-sm-10" style="width: 70%;">
            <textarea class="form-control" rows="3" id="create-describe"></textarea>
        </div>
    </div>

    <div class="form-group">
        <label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
        <div class="col-sm-10" style="width: 70%;">
            <textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
        </div>
    </div>

    <div class="form-group">
        <label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control myDateClass" id="create-nextContactTime">
        </div>
    </div>

</form>
</body>
</html>