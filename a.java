<script type="text/javascript" language="javascript">
����function Ceshi()
����{
��������var a = "<%=Getstr()%>";
��������alert(a);
����}
</script>
<input type="button" onclick="Ceshi();" value="js���ú�̨����" /> 
��̨����
public string Getstr()
{
����string aa = "ѧ������APP��";
����return aa;
}