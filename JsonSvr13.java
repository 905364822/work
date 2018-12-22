package web.ft.json;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;


/**
 * 
 */
public abstract class JsonSvr13 extends ci.page.CiPageObject {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public JsonSvr13() {

    }

    /***
     * ҳ�����ʱִ�еĴ���
     */
    public void pageLoad() throws javax.servlet.ServletException, java.io.IOException {

        //��ӡָ��
        String action = ci.page.Utils.getParaStr(request, "action", "");
        if(action.equals("print")){
            String id = ci.page.Utils.getParaStr(request, "id", "");
            Object obj = session.getAttribute(id);
            session.removeAttribute(id);
            if(obj!=null){
                String msg = obj.toString();
                net.sf.json.JSONObject jsonMsg = net.sf.json.JSONObject.fromObject(msg);
              processPrint(jsonMsg);
            }
            return;
        }


        //��������
        javax.servlet.jsp.PageContext pageContext = javax.servlet.jsp.JspFactory.getDefaultFactory().getPageContext(this, request, response, null, true, 8192, true);
        javax.servlet.jsp.JspWriter jspWriter = pageContext.getOut();
        jspWriter.clear();

        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        response.setHeader("CicurrentTime", new java.text.SimpleDateFormat("yyyy-MM-dd mm:hh:ss:SSSS").format(new java.util.Date()));
        response.setDateHeader("Expires", 0);
        response.setHeader("Access-Control-Allow-Origin", "*");

        net.sf.json.JSONObject jsonResult = new net.sf.json.JSONObject();
        net.sf.json.JSONObject jsonMsg = new net.sf.json.JSONObject();

        // ��ȡ��ǰ��¼ϵͳ���û����󣬿ɴ��еõ��û�������Ϣ�ͽ�ɫ��Ȩ�ȡ�
        // δ��¼ϵͳ�����������쳣�����·���ʧ�ܣ��Ӷ���Ȩ�޿������ã���˶�����Ҫ��¼���Ƶ�ҳ�棬��һ��Ҫ�������䡣
        try {


            String jsonMsgStr = ci.page.Utils.getParaStr(request, "msg", "");

            if (ci.comm.StringUtils.isEmptyOrBlank(jsonMsgStr)) {
                jsonMsg.put("issucc", "ERROR");
                jsonMsg.put("errmessage", "<CIF-30000><û���ҵ�ָ���Ĳ�����>");
            } else {

                // String(request.getParameter("msg").getBytes("ISO8859-1"), "UTF-8");

                jsonMsgStr = new String(new sun.misc.BASE64Decoder().decodeBuffer(jsonMsgStr));
                jsonMsgStr = java.net.URLDecoder.decode(jsonMsgStr, "UTF-8");
                jsonMsg = net.sf.json.JSONObject.fromObject(jsonMsgStr);

                String clientcmd = jsonMsg.getString("clientcmd");
                if (clientcmd.equals("U") || clientcmd.equals("A")) {
                    processUpdate(jsonResult, jsonMsg);
                } else if (clientcmd.equals("D")) {
                    processDelete(jsonResult, jsonMsg);
                } else if (clientcmd.equals("G")) {
                    processGetDataRows(jsonResult, jsonMsg);
                }else if (clientcmd.equals("SETSESSION")) {
                    processSetSession(jsonResult, jsonMsg);
                }else if (clientcmd.equals("GETSESSION")) {
                    processGetSession(jsonResult, jsonMsg);
                }else {
                    jsonMsg.put("issucc", "ERROR");
                    jsonMsg.put("errmessage", "clientCmd��������ȷ��");
                }
            }
        } catch (Exception e) {
            jsonMsg.put("issucc", "ERROR");
            jsonMsg.put("errmessage", e.getMessage());
        }

        jsonResult.put("result", jsonMsg);
        String strForOut = jsonResult.toString();
        response.getWriter().print(strForOut);
    }


    /***
     * ���ݷ�ҳ����
     * 
     * @param taskId
     * @throws java.io.IOException
     */
    private void processGetDataRows(net.sf.json.JSONObject jsonResult, net.sf.json.JSONObject jsonMsg) throws java.io.IOException {

        net.sf.json.JSONArray jsonDataRows = new net.sf.json.JSONArray();
        net.sf.json.JSONArray jsonDataCols = new net.sf.json.JSONArray();

        try {
            int cipagesize = Integer.parseInt((String) jsonMsg.get("cipagesize"));
            int cipageindex = Integer.parseInt((String) jsonMsg.get("cipageindex"));
            String citblselcls = (String) jsonMsg.get("citblselcls");
            String citblfrmcls = (String) jsonMsg.get("citblfrmcls");
            String citblwhecls = (String) jsonMsg.get("citblwhecls");
            String citblordcls = (String) jsonMsg.get("citblordcls");

            // ��ȡ���ݱ�ķ�ҳ����
            ci.adp.TablePager tablePager = null;
            tablePager = ci.adp.Query.getTablePager(cipagesize, cipageindex, citblselcls, citblfrmcls, citblwhecls, null, null, citblordcls);

            // ������ת��Ϊjson����

            if (tablePager == null) {
                jsonMsg.put("issucc", "ERROR");
                jsonMsg.put("errmessage", "SQL����citblselcls:" + citblselcls + "��citblfrmcls��" + citblfrmcls + "citblwhecls" + citblwhecls + "��citblordcls��" + citblordcls);
            } else {

                for (int i = 0; i < tablePager.Rows.length; i++) {
                    net.sf.json.JSONObject jsonDataRow = new net.sf.json.JSONObject();
                    ci.adp.DataRow dataRow = tablePager.getDataRowByIndex(i);
                    for (int j = 0; j < tablePager.Cols.length; j++) {
                        String colName = tablePager.Cols[j];
                        String colVal = dataRow.getValueStr(tablePager.Cols[j]);
                        jsonDataRow.put(colName, colVal);
                    }
                    jsonDataRows.add(jsonDataRow);
                }

                int colIndex = 0;
                for (String colName : tablePager.Cols) {

                    net.sf.json.JSONObject jsonDataCol = new net.sf.json.JSONObject();
                    jsonDataCol.put("colname", colName);
                    jsonDataCol.put("coltype", tablePager.ColTypes[colIndex]);
                    jsonDataCol.put("collength", tablePager.ColLens[colIndex]);
                    colIndex++;
                    jsonDataCols.add(jsonDataCol);
                }
                jsonMsg.put("cipagecount", tablePager.pageCount);
                jsonMsg.put("cirowcount", tablePager.queryRowCount);
                jsonMsg.put("cipageindex", tablePager.pageIndex);

                jsonResult.put("cirows", jsonDataRows);
                jsonResult.put("cicols", jsonDataCols);
                jsonMsg.put("issucc", "OK");
            }
        } catch (Exception e) {
            jsonMsg.put("issucc", "ERROR");
            jsonMsg.put("errmessage", e.toString());
        }

    }

    /**
     * ��ȡ�Ự����
     * @param jsonResult
     * @param jsonMsg
     */
    private void processGetSession(net.sf.json.JSONObject jsonResult, net.sf.json.JSONObject jsonMsg) {
        Object[] fldnames = ((net.sf.json.JSONArray)jsonMsg.get("fldnames")).toArray();

        java.util.ArrayList<Object> values = new java.util.ArrayList<Object>() ;

        for(Object keyName: fldnames){
            String keyNameStr = (String)keyName;
            if(keyNameStr==null)keyNameStr="";
            Object obj =request.getSession().getAttribute(keyNameStr);
            values.add(obj);
        }
        jsonMsg.remove("fldvals");
        jsonMsg.put("fldvals",values.toArray());
        if(values.size()==0)jsonMsg.put("sessionVal","");
        else jsonMsg.put("sessionVal", values.toArray()[0]);
        jsonMsg.put("issucc", "OK");

    }

    /**
     * ���ûỰ����
     * @param jsonResult
     * @param jsonMsg
     */
    private void processSetSession(net.sf.json.JSONObject jsonResult, net.sf.json.JSONObject jsonMsg) {

        Object[] fldnames = ((net.sf.json.JSONArray) jsonMsg.get("fldnames")).toArray();
        Object[] fldvals = ((net.sf.json.JSONArray) jsonMsg.get("fldvals")).toArray();

        if(fldnames.length!=fldvals.length){
            jsonMsg.put("errmessage", "�Ự���ֺ�ֵ����������ͬ��");
            jsonMsg.put("issucc", "ERROR");
        }

        int index =0;
        for(Object keyName: fldnames){
            String keyNameStr = (String)keyName;
            if(keyNameStr==null)keyNameStr="";
            request.getSession().setAttribute(keyNameStr, fldvals[index]);
            index++;
        }
        jsonMsg.put("issucc", "OK");
    }

    /**
     * ִ�д�ӡ����
     * @param jsonResult
     * @param jsonMsg
     * @throws IOException 
     */
    private void processPrint(net.sf.json.JSONObject jsonMsg) throws IOException {
        java.io.PrintWriter out = response.getWriter();

        net.sf.json.JSONArray fields = jsonMsg.getJSONArray("fields");
        String title = jsonMsg.getString("title");
        String filname = jsonMsg.getString("filename").toLowerCase();
        if(filname.endsWith("doc"))response.setHeader("Content-Type", "applcation/msword");
        else if(filname.endsWith("xls"))response.setHeader("Content-Type", "applcation/vnd.ms-excel");
        response.setHeader("content-disposition", "attachment;filename="+filname);

        String citblselcls = (String) jsonMsg.get("citblselcls");
        String citblfrmcls = (String) jsonMsg.get("citblfrmcls");
        String citblwhecls = (String) jsonMsg.get("citblwhecls");
        String citblordcls = (String) jsonMsg.get("citblordcls");

        // ��ȡ���ݱ�ķ�ҳ����
        ci.adp.TablePager tablePager = null;
        tablePager = ci.adp.Query.getTablePager(Integer.MAX_VALUE, 0, citblselcls, citblfrmcls, citblwhecls, null, null, citblordcls);

        // ������ת��Ϊjson����

        if (tablePager == null) {
            out.write("<!DOCTYPE HTML>");
            out.write("<html>");
            out.write("<head>");
            out.write("<meta http-equiv=\"pragma\" content=\"no-cache\" />");
            out.write("<meta http-equiv=\"cache-control\" content=\"no-cache\" />");
            out.write("<meta http-equiv=\"expires\" content=\"0\" />");
            out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
            out.write("<title>"+title+"</title>");
            out.write("</head>");
            out.write("<body>");
            out.write("SQL����citblselcls:" + citblselcls + "��citblfrmcls��" + citblfrmcls + "citblwhecls" + citblwhecls + "��citblordcls��" + citblordcls);
            out.write("</tbody>");
            out.write("</table>");
            out.write("</body>");
            out.write("</html>");       
            return;
        } 

        String[] fieldNames = new String[fields.size()];
        String[] fieldTitle = new String[fields.size()];
        String[] fieldWidth = new String[fields.size()];
        String[] fieldFormat = new String[fields.size()];

        for(int i=0;i<fields.size(); i++){
             net.sf.json.JSONObject field =  fields.getJSONObject(i);
             fieldNames[i]= field.getString("name");
             if(field.has("name")){
                 fieldNames[i]= field.getString("name");
             }else{
                 fieldNames[i]=null;
             }
             if(field.has("title")){
                 fieldTitle[i]= field.getString("title");
             }else{
                 fieldTitle[i]=null;
             }
             if(field.has("width")){
                 fieldWidth[i]= field.getString("width");
             }else{
                 fieldWidth[i]=null;
             }
             if(field.has("format")){
                 fieldFormat[i]= field.getString("format");
             }else{
                 fieldFormat[i]=null;
             }
        }


        //new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 

        out.write("<!DOCTYPE HTML>");
        out.write("<html>");
        out.write("<head>");
        out.write("<meta http-equiv=\"pragma\" content=\"no-cache\" />");
        out.write("<meta http-equiv=\"cache-control\" content=\"no-cache\" />");
        out.write("<meta http-equiv=\"expires\" content=\"0\" />");
        out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
        out.write("<title>"+title+"</title>");
        out.write("<style type=\"text/css\">table{font-size:16px;width:100%; min-width:640px; text-align:center; margin-left:auto;margin-right:auto; border-collapse:collapse; border: 1px solid #000000;}th,td {font-size:16px;padding:6px; text-align:center; border-collapse:collapse; border: 1px solid #000000;}</style>");
        out.write("</head>");
        out.write("<body style=\"text-align:center;\">");
        out.write("<div style=\"text-align:center; padding-bottom:20px; font-size:24px;font-family:Microsoft YaHei; font-weight:bold;\">"+title+"</div>");
        out.write("<div style=\"text-align:center;\"><table cellspacing=\"0\" border=\"1px\" rules=\"all\" >");
        out.write("<thead>");
        out.write("<tr>");
        for(int i=0;i<fieldTitle.length; i++){
                out.write("<th");
                if(fieldWidth[i]!=null)out.write(" style=\""+fieldWidth[i]+"\"");
                out.write(">");
                out.write(fieldTitle[i]);
                out.write("</th>");
        }
        out.write("</tr>");
        out.write("</thead>");
        out.write("<tbody>");
        for(int index=0; index< tablePager.Rows.length; index++){
            out.write("<tr>");
            ci.adp.DataRow row = tablePager.getDataRowByIndex(index);
            for(int j=0;j<fieldNames.length; j++){
                out.write("<td>");
                int colIndex = row.rowTable.getColIndex(fieldNames[j]);
                String colValue=row.getValueStr(fieldNames[j]);
                if(row.rowTable.ColTypes[colIndex]== java.sql.Types.DATE || row.rowTable.ColTypes[colIndex]== java.sql.Types.TIMESTAMP ){
                    if(fieldFormat[j]!=null){
                        java.text.SimpleDateFormat simpleDateFormat = new SimpleDateFormat(fieldFormat[j]);
                        if(colValue!=""){
                            try {
                                colValue = simpleDateFormat.format(simpleDateFormat.parse(colValue));
                            } catch (ParseException e) {
                                colValue=row.getValueStr(fieldNames[j]);
                            }
                        }
                    }
                }

                out.write(colValue);
                out.write("</td>");
            }
            out.write("</tr>");
        }
        out.write("</tbody>");
        out.write("</table></div>");
        out.write("</body>");
        out.write("</html>");
    }

    /***
     * ���ݿ���²���
     * @param jsonResult
     * @param jsonMsg
     * @throws java.io.IOException
     */
    private void processUpdate(net.sf.json.JSONObject jsonResult, net.sf.json.JSONObject jsonMsg) throws java.io.IOException {

        String clientcmd = (String) jsonMsg.get("clientcmd");
        String citblname = (String) jsonMsg.get("citblname");
        String citblwhecls = (String) jsonMsg.get("citblwhecls");
        Object[] fldnames = ((net.sf.json.JSONArray) jsonMsg.get("fldnames")).toArray();
        Object[] fldvals = ((net.sf.json.JSONArray) jsonMsg.get("fldvals")).toArray();


        ci.adp.Table table = null;
        ci.adp.DataRow datarow = null;
        String result=null;
        try {
            if (clientcmd.equals("A")) {

                ci.adp.Table tabletest = ci.adp.Table.getInstance(citblname);
                boolean hasTrid = false;
                for(String col: tabletest.Cols){
                    if(col.equals("TRID")){
                        hasTrid =true;break;
                    }
                }
                if(!hasTrid){
                    ci.adp.Query.execute("ALTER TABLE ADD COLUMN TRID INT");
                    ci.adp.Query.execute("ALTER TABLE ADD COLUMN TRNO INT");
                }
                table = ci.adp.Table.getInstance(citblname);
                datarow = table.newDataRow();

                result=setDataRowValue(datarow, fldnames, fldvals);
                datarow.update();
            } else {
                table = ci.adp.Table.getInstance(citblname).getTableWhere(citblwhecls);
                for (int rowIndex = 0; rowIndex < table.Rows.length; rowIndex++) {
                    datarow = table.getDataRowByIndex(rowIndex);
                    result=setDataRowValue(datarow, fldnames, fldvals);
                    datarow.update();
                }
            }
            if(result==null){
                jsonMsg.put("issucc", "OK");
            }else{
                jsonMsg.put("issucc", "ERROR");
                jsonMsg.put("errmessage", result);
            }
        } catch (Exception e) {
            jsonMsg.put("issucc", "ERROR");
            jsonMsg.put("errmessage", e.toString());
        }

    }

    /***
     * Ϊdatarow��������ֵ
     * 
     * @param dataRow
     * @param fldnames
     * @param fldvals
     */
    public String setDataRowValue(ci.adp.DataRow dataRow, Object[] fldnames, Object[] fldvals) {
        String result = "";
        for (int i = 0; i < fldnames.length; i++) {
            String colName = (String) fldnames[i];
            String colVal = (String)fldvals[i];
            int colIndex = dataRow.rowTable.getColIndex(colName);
            if(colIndex==-1){
                result+=String.format("��������ʱ�����ݱ���û�з���ָ����������%s�����Ѿ����ԣ�", colName);
                continue;
            }
            int colType = dataRow.rowTable.ColTypes[colIndex];
            if (colType== java.sql.Types.INTEGER || colType == java.sql.Types.NUMERIC) {
                dataRow.setValue(colName, Integer.parseInt(colVal));
            } else if (colType == java.sql.Types.VARCHAR) {
                dataRow.setValue(colName, colVal);
            } else if (colType == java.sql.Types.DOUBLE) {
                dataRow.setValue(colName, Double.valueOf(colVal));
            } else if (colType == java.sql.Types.DECIMAL) {
                Double val = Double.valueOf(colVal);
                dataRow.setValue(colName,  java.math.BigDecimal.valueOf(val));
            } else if (colType == java.sql.Types.DATE) {
                try {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");  
                    dataRow.setValue(colName,sdf.parse(colVal));
                } catch (Exception e) {
                    dataRow.setValue(colName, new java.util.Date());
                }
            } else if (colType == java.sql.Types.TIMESTAMP) {
                try {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");  
                    dataRow.setValue(colName,sdf.parse(colVal));
                } catch (Exception e) {
                    dataRow.setValue(colName, new java.util.Date());
                }
            } else if (colType == java.sql.Types.CLOB) {
                if (fldvals[i] != null) {
                    dataRow.setValue(colName, colVal);
                }
            }
        }
        return result;
    }

    /***
     * 
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static java.util.Date convertData(String dateStr) throws java.text.ParseException {
        java.util.Date date = null;
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.text.SimpleDateFormat sdf2 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = sdf2.parse(dateStr);
        } catch (Exception e) {
            date = sdf.parse(dateStr);
        }
        return date;
    }

    /**
     * ����ɾ������
     * 
     * @param citblrowid
     * @param citblname
     * @param clientCmd
     * @throws java.io.IOException
     */
    private void processDelete(net.sf.json.JSONObject jsonResult, net.sf.json.JSONObject jsonMsg) throws java.io.IOException {
        // TOFIX:������

        String citblname = (String) jsonMsg.get("citblname");
        String citblwhecls = (String) jsonMsg.get("citblwhecls");

        ci.adp.Table table = ci.adp.Table.getInstance(citblname);
        try {
            table.deleteRowByWhere(citblwhecls);
            jsonMsg.put("issucc", "OK");
        } catch (Exception e) {
            jsonMsg.put("issucc", "ERROR");
            jsonMsg.put("errmessage", e.getMessage());
        }

    }



    /***
     * �����ɼ��Ŀ��ַ�ת��Ϊ���кͿ��У��Ա���������������
     * 
     * @param str
     * @return
     */
    public String replaceEmptyChar(String str) {

        str = str.replaceAll("\\r\\n", "<br/>");
        str = str.replaceAll("\\r", "<br/>");
        str = str.replaceAll("\\n", "<br/>");
        str = str.replace(" ", "&nbsp;");

        return str;
    }

    /**
     * ҳ��������ʱ�������´���
     */
    public void pageUnLoad() throws javax.servlet.ServletException, java.io.IOException {

    }

}