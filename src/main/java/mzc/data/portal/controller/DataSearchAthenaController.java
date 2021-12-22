package mzc.data.portal.controller;

import mzc.data.portal.dto.ApiResponse;
import mzc.data.portal.dto.DataSearchAthenaData;
import mzc.data.portal.dto.UserData;
import com.google.gson.Gson;
import mzc.data.portal.agent.dto.DatabaseData;
import mzc.data.portal.agent.dto.TableData;
import mzc.data.portal.agent.service.AthenaAgentService;
import mzc.data.portal.dto.QueryData;
import mzc.data.portal.service.DataSearchAthenaService;
import mzc.data.portal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


import java.util.List;

@Controller
public class DataSearchAthenaController {
    
    private DataSearchAthenaService dataService;
    private UserService userService;
    private AthenaAgentService athenaAgentService;

    /**
     * 생성자 주입
     * @param dataService
     * @param userService
     * @param athenaAgentService
     */
    @Autowired
    public DataSearchAthenaController(DataSearchAthenaService dataService, UserService userService, AthenaAgentService athenaAgentService){
        this.dataService = dataService;
        this.userService = userService;
        this.athenaAgentService = athenaAgentService;
    }



    @GetMapping("/data-search-athena")
    public String dataSearch(Model model) {

        List<DataSearchAthenaData> dataSearchData = dataService.dataSearch();

        List<DatabaseData> databases = athenaAgentService.databases();

        List<QueryData> queryData = dataService.queryLog();

        model.addAttribute("queryLog", queryData);
        model.addAttribute("databases", databases);
        model.addAttribute("dataSearch", dataSearchData);
        return ("user/data-search-athena");
    }

    @PostMapping("/data-search-athena")
    @ResponseBody
    public DataSearchAthenaData.DataSearchCustomRes addDataSearchInfo(@Valid DataSearchAthenaData.AddDataSearchParam param, Model model) {

        UserData loginData = userService.getLoginUser();
        UserData userData = userService.getUser(loginData.getIdx());
        param.setUserIdx(userData.getIdx());
        //param.setUserIdx(param.getDataSearchIdx());
        dataService.addDataSearch(param);

        return new DataSearchAthenaData.DataSearchCustomRes<>(ApiResponse.OK, null, null);
    }

    @PostMapping("/data-search-athena/delete")
    @ResponseBody
    public DataSearchAthenaData.DataSearchCustomRes deleteDataSearchInfo(@Valid DataSearchAthenaData.DeleteDataSearchParam param, Model model){

        UserData loginData = userService.getLoginUser();
        UserData userData = userService.getUser(loginData.getIdx());
        param.setUserIdx(userData.getIdx());
        dataService.deleteDataSearch(param);
        return new DataSearchAthenaData.DataSearchCustomRes<>(ApiResponse.OK, null, null);
    }

    @PostMapping("/data-query")
    @ResponseBody
    public QueryData.MetaSimpleQueryDataObjRes executeQuery(@Valid QueryData.QueryDataParam param, Model model) {

        UserData loginData = userService.getLoginUser();
        UserData userData = userService.getUser(loginData.getIdx());
        param.setUserIdx(userData.getIdx());
        QueryData.MetaSimpleQueryData metaSimpleQueryData = athenaAgentService.simpleQuery(param);

        return new QueryData.MetaSimpleQueryDataObjRes(ApiResponse.OK, null, metaSimpleQueryData);
    }


    @GetMapping("/data-schema")
    @ResponseBody
    public String dataSchema(@RequestParam String schema) {
       List<TableData> tables = athenaAgentService.tables(schema);
        return new Gson().toJson(tables);

    }

    @PostMapping("/data-table")
    @ResponseBody
    public String dataTable(@Valid TableData.GetTableParam param) {
        TableData table = athenaAgentService.table(param);
        return new Gson().toJson(table);
    }

    //데이터 조회-> Results 미리보기
    @PostMapping("/data-preview")
    @ResponseBody
    public QueryData.MetaSimpleQueryDataObjRes dataPreview(@Valid QueryData.QueryDataPreviewParam param, Model model) {

        String schema = param.getSchema();
        String table = param.getTable();
        String query = "select * from " + schema + "." + table + " limit 10";
        param.setDataSearchQuery(query);

        UserData loginData = userService.getLoginUser();
        UserData userData = userService.getUser(loginData.getIdx());
        param.setUserIdx(userData.getIdx());
        QueryData.MetaSimpleQueryData metaSimpleQueryData = athenaAgentService.simplePreviewQuery(param);

        return new QueryData.MetaSimpleQueryDataObjRes(ApiResponse.OK, null, metaSimpleQueryData);
    }
}
