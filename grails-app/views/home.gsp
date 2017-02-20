<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>宽度自适应布局</title>
	<script type="text/javascript" src="js/jquery.min.js"></script>
	<script type="text/javascript">
		$.ajax({
				type:"post",
				//async:true,	//异步请求（同步请求将会锁住浏览器，用户其他操作必须等待请求完成才可以执行）
				url:"getMemory.action",
				dataType:"json",
				success:function(data){
					alert("ok");
					//myChart.hideLoading();    //隐藏加载动画
					option.series[0].data[0].value=data[0].memPercent;
					//option.series[0].data[0].value=data[0].memPercent;
					myChart.setOption(option, true);
				},
				error:function(jqXHR){
					alert("请求失败");
					myChart.hideLoading();
				}
			});
	</script>
</head>
<body>
	<div>aaa</div>
</body>
</html>