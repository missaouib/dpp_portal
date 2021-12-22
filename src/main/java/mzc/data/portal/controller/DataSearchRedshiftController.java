package mzc.data.portal.controller;

import com.google.gson.Gson;
import mzc.data.portal.agent.dto.DatabaseData;
import mzc.data.portal.agent.dto.SchemaData;
import mzc.data.portal.agent.dto.TableData;
import mzc.data.portal.agent.service.RedshiftAgentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import mzc.data.portal.dto.*;
import mzc.data.portal.service.DataSearchRedshiftService;
import mzc.data.portal.service.UserService;
import org.springframework.ui.Model;

import javax.validation.Valid;
import java.util.List;

@Controller
public class DataSearchRedshiftController {

    private DataSearchRedshiftService dataSearchRedshiftService;
    private UserService userService;
    private RedshiftAgentService redshiftAgentService;

    /**
     * 생성자 주입
     * @param dataSearchRedshiftService
     * @param userService
     * @param redshiftAgentService
     */
    @Autowired
    public DataSearchRedshiftController(DataSearchRedshiftService dataSearchRedshiftService, UserService userService, RedshiftAgentService redshiftAgentService){
        this.dataSearchRedshiftService = dataSearchRedshiftService;
        this.userService = userService;
        this.redshiftAgentService = redshiftAgentService;
    }

    @GetMapping("/data-search-redshift")
    public String main(Model model) {

        List<DataSearchRedshiftData> dataSearchRedshiftData = dataSearchRedshiftService.dataSearchRedshift();
        List<QueryRedshiftData> queryRedshiftData = dataSearchRedshiftService.queryLog();

        List<DatabaseData> databases = redshiftAgentService.databases();

        model.addAttribute("queryLog", queryRedshiftData);
        model.addAttribute("dataSearchRedshift", dataSearchRedshiftData);
        model.addAttribute("databases", databases);
        return "user/data-search-redshift";
    }

    @PostMapping("/data-search-redshift")
    @ResponseBody
    public DataSearchRedshiftData.DataSearchCustomRes addDataSearchInfo(@Valid DataSearchRedshiftData.AddDataSearchParam param, Model model) {

        UserData loginData = userService.getLoginUser();
        UserData userData = userService.getUser(loginData.getIdx());
        param.setUserIdx(userData.getIdx());

        dataSearchRedshiftService.addDataSearch(param);

        return new DataSearchRedshiftData.DataSearchCustomRes<>(ApiResponse.OK, null, null);
    }

    @PostMapping("/data-search-redshift/delete")
    @ResponseBody
    public DataSearchRedshiftData.DataSearchCustomRes deleteDataSearchInfo(@Valid DataSearchRedshiftData.DeleteDataSearchParam param, Model model){

        UserData loginData = userService.getLoginUser();
        UserData userData = userService.getUser(loginData.getIdx());
        param.setUserIdx(userData.getIdx());
        dataSearchRedshiftService.deleteDataSearch(param);
        return new DataSearchRedshiftData.DataSearchCustomRes<>(ApiResponse.OK, null, null);
    }


    @PostMapping("/data-query-redshift")
    @ResponseBody
    public QueryRedshiftData.MetaSimpleQueryDataObjRes executeQuery(@Valid QueryRedshiftData.QueryDataParam param, Model model) {

        UserData loginData = userService.getLoginUser();
        UserData userData = userService.getUser(loginData.getIdx());
        param.setUserIdx(userData.getIdx());
        QueryRedshiftData.MetaSimpleQueryData metaSimpleQueryData = redshiftAgentService.simpleQuery(param);

        return new QueryRedshiftData.MetaSimpleQueryDataObjRes(ApiResponse.OK, null, metaSimpleQueryData);
    }

    @GetMapping("/data-search-redshift-schemas")
    @ResponseBody
    public String dataSchema(@RequestParam String database) {
        //List<DatabaseData> databases = redshiftAgentService.databases();
        List<SchemaData> schemas = redshiftAgentService.schemas(database);
        return new Gson().toJson(schemas);

    }

    @GetMapping("/data-search-redshift-tables")
    @ResponseBody
    public String dataTables(@RequestParam String database, @RequestParam String schema) {
        List<TableData> tables = redshiftAgentService.tables(database, schema);
        return new Gson().toJson(tables);
    }

    @PostMapping("/data-search-redshift-table")
    @ResponseBody
    public String dataTable(@Valid TableData.GetTableParam param) {
        TableData table = redshiftAgentService.table(param);
        return new Gson().toJson(table);
    }

    @GetMapping("/data-search-redshift/execute")
    @ResponseBody
    public String dataSearchExecute(@Valid QueryRedshiftData.QueryDataParam param) {
        QueryRedshiftData.MetaSimpleQueryData query = redshiftAgentService.simpleQuery(param);
        return new Gson().toJson(query);
    }

    @PostMapping("/data-preview-redshift")
    @ResponseBody
    public QueryRedshiftData.MetaSimpleQueryDataObjRes dataPreview(@Valid QueryRedshiftData.QueryDataPreviewParam param, Model model) {

        String database = param.getDatabase();
        String schema = param.getSchema();
        String table = param.getTable();
        String query = "select * from " + database + "." + schema + "." + table + " limit 10";
        param.setDataSearchQuery(query);

        UserData loginData = userService.getLoginUser();
        UserData userData = userService.getUser(loginData.getIdx());
        param.setUserIdx(userData.getIdx());
        QueryRedshiftData.MetaSimpleQueryData metaSimpleQueryData = redshiftAgentService.simplePreviewQuery(param);

        return new QueryRedshiftData.MetaSimpleQueryDataObjRes(ApiResponse.OK, null, metaSimpleQueryData);
    }

}
