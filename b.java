1 <script>
2     function CallCs() {
3         var str = "<%=GetStr()%>";
4         alert(str);
5     }
6 </script>
7 
8 <input type="button" name="btnClick" value="js调用后台代码" onclick="CallCs();" />