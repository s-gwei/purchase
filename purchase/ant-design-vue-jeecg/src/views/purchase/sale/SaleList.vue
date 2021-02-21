<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form   @keyup.enter.native="searchQuery" layout="inline">
        <a-row :gutter="24">
             <a-col :md="6" :sm="8">
            <a-form-item label="客户">
           
                <a-select  name="projectNameList" v-model="queryParam.customerId" placeholder="请选择客户" >
                        <a-select-option value="">请选择</a-select-option>
                        <a-select-option v-for="item in couList" :key="item.id" :value="item.id" >{{item.shopName}}</a-select-option>
                    </a-select>
            </a-form-item>
          </a-col>
            <a-col :md="6" :sm="8">
            <a-form-item label="商品">
                     <a-select  name="projectNameList" v-model="queryParam.productId" placeholder="请选择产品" >
                        <a-select-option value="">请选择</a-select-option>
                        <a-select-option v-for="item in proList" :key="item.id" :value="item.id" >{{item.name}}</a-select-option>
                    </a-select>
            </a-form-item>
             </a-col>
                  <a-col :md="6" :sm="12">
            <a-form-item label="创建时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-range-picker
                style="width: 210px"
                v-model="queryParam.createTimeRange"
                format="YYYY-MM-DD"
                :placeholder="['开始时间', '结束时间']"
                @change="onDateChange"
                @ok="onDateOk"
              />
            </a-form-item>
            </a-col>
        
             <a-col :md="6" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
             
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增销售订单</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('出货订单表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
      
      </a-upload>
    <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        :scroll="{x:true}"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="" style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="downloadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <sale-modal ref="modalForm" @ok="modalFormOk"></sale-modal>
  </a-card>
</template>

<script>

  import { filterObj } from '@/utils/util';
  import { httpAction, getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import SaleModal from './modules/SaleModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'

  export default {
    name: 'SaleList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      SaleModal,
      JSuperQuery,
    },
    data () {
      return {
        queryParam: {
          customerId:'',
          createTimeRange:[],
          productId:'',
        },
        form: [],
         couList:[],
        proList:[],
        description: '出货订单表管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
           {
            title:'客户店名',
            align:"center",
            dataIndex: 'customerName'
          },
          {
            title:'产品名称',
            align:"center",
            dataIndex: 'productName'
          },
          // {
          //   title:'产品id',
          //   align:"center",
          //   dataIndex: 'productId'
          // },
          {
            title:'单位',
            align:"center",
            dataIndex: 'company'
          },
          {
            title:'单价',
            align:"center",
            dataIndex: 'unitPrice'
          },
          {
            title:'数量',
            align:"center",
            dataIndex: 'account'
          },
          {
            title:'总价',
            align:"center",
            dataIndex: 'totalPrice'
          },
          {
            title:'创建时间',
            align:"center",
            dataIndex: 'createTime'
          },
          // {
          //   title:'客户id',
          //   align:"center",
          //   dataIndex: 'customerId'
          // },
         
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/purchase/sale/list",
          delete: "/purchase/sale/delete",
          deleteBatch: "/purchase/sale/deleteBatch",
          exportXlsUrl: "/purchase/sale/exportXls",
          importExcelUrl: "purchase/sale/importExcel",
          queryCouList:"/purchase/customer/queryList",
          queryProList:"/purchase/product/queryList"
          
        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
    this.getSuperFieldList();
      this.queryCouList();
      this.queryProList();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
        getQueryParams(){
            console.info(this.queryParam);
        var param = Object.assign({}, this.queryParam,this.isorter);
        param.field = this.getQueryField();
        param.pageNo = this.ipagination.current;
        param.pageSize = this.ipagination.pageSize;
        delete param.createTimeRange; // 时间参数不传递后台
        if (this.superQueryParams) {
          param['superQueryParams'] = encodeURI(this.superQueryParams)
          param['superQueryMatchType'] = this.superQueryMatchType
        }
        return filterObj(param);
      },
      // 查询表单数据
      // searchQuery(){
      //  this.queryParam
      //     getAction(this.url.list).then((res)=>{
      //      console.info(res);
      //      this.proList = res.result;
      //      console.info(this.proList);
      //     });
      // },
            //查询产品信息
      queryProList(){
           getAction(this.url.queryProList).then((res)=>{
           console.info(res);
           this.proList = res.result;
           console.info(this.proList);
          });
      },
      //查询客户信息
      queryCouList(){
          getAction(this.url.queryCouList).then((res)=>{
           console.info(res);
           this.couList = res.result;
           console.info(this.couList);
          });
      },
      //查询时间段
      onDateChange: function (value, dateString) {
        console.log(dateString[0],dateString[1]);
        this.queryParam.createTime_begin=dateString[0];
        this.queryParam.createTime_end=dateString[1];
      },
      onDateOk(value) {
        console.log(value);
      },
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'productName',text:'产品名称',dictCode:''})
        fieldList.push({type:'string',value:'productId',text:'产品id',dictCode:''})
        fieldList.push({type:'string',value:'company',text:'单位',dictCode:''})
        fieldList.push({type:'double',value:'unitPrice',text:'单价',dictCode:''})
        fieldList.push({type:'double',value:'account',text:'数量',dictCode:''})
        fieldList.push({type:'double',value:'totalPrice',text:'总价',dictCode:''})
        fieldList.push({type:'string',value:'customerId',text:'客户id',dictCode:''})
        fieldList.push({type:'string',value:'customerName',text:'客户店名',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>