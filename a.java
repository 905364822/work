<script type="text/javascript" language="javascript">
　　function Ceshi()
　　{
　　　　var a = "<%=Getstr()%>";
　　　　alert(a);
　　}
</script>
<input type="button" onclick="Ceshi();" value="js调用后台代码" /> 
后台代码
public string Getstr()
{
　　string aa = "学生记账APP！";
　　return aa;
}