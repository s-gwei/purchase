<template>
  <div class="page-header-index-wide">
    <a-row :gutter="24">
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="当日销售额" total="" >
          <h1>{{saleInfo.dailySales}}元</h1>
          <!-- <a-tooltip title="指标说明" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <div>
            <trend flag="up" style="margin-right: 16px;">
              <span slot="term">周同比</span>
              12%
            </trend>
            <trend flag="down">
              <span slot="term">日同比</span>
              11%
            </trend> -->
          <!-- </div> -->
          <!-- <template slot="footer">日均销售额<span>{{saleInfo.dailySales}}</span></template> -->
        </chart-card>
      </a-col>
            <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="当日利润" total="" >
          <h1 >{{saleInfo.dailyProfit}}元</h1>
        </chart-card>
      </a-col>
               <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="当月销售额" total="" >
          <h1>{{saleInfo.salesMonth}}元</h1>
        </chart-card>
      </a-col>
               <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="当月利润" total="" >
          <h1>{{saleInfo.profitMonth}}元</h1>
        </chart-card>
      </a-col>
      <!-- <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="订单量" :total="8846 | NumberFormat">
          <a-tooltip title="指标说明" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <div>
            <mini-area />
          </div>
          <template slot="footer">日订单量<span> {{ '1234' | NumberFormat }}</span></template>
        </chart-card>
      </a-col> -->
      <!-- <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="支付笔数" :total="6560 | NumberFormat">
          <a-tooltip title="指标说明" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <div>
            <mini-bar :height="40" />
          </div>
          <template slot="footer">转化率 <span>60%</span></template>
        </chart-card>
      </a-col> -->
      <!-- <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="运营活动效果" total="78%">
          <a-tooltip title="指标说明" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <div>
            <mini-progress color="rgb(19, 194, 194)" :target="80" :percentage="78" :height="8" />
          </div>
          <template slot="footer">
            <trend flag="down" style="margin-right: 16px;">
              <span slot="term">同周比</span>
              12%
            </trend>
            <trend flag="up">
              <span slot="term">日环比</span>
              80%
            </trend>
          </template>
        </chart-card>
      </a-col> -->
    </a-row>

    <a-card :loading="loading" :bordered="false" :body-style="{padding: '0'}">
      <div class="salesCard">
        <a-tabs default-active-key="1" size="large" :tab-bar-style="{marginBottom: '24px', paddingLeft: '16px'}">
          <!-- <div class="extra-wrapper" slot="tabBarExtraContent">
             <div class="extra-item">
              <a>今日</a>
              <a>本周</a>
              <a>本月</a>
              <a>本年</a>
            </div> 
            <a-range-picker :style="{width: '256px'}" />
          </div> -->
             <a-tab-pane loading="true" tab="毛利润" key="1">
            <a-row>
              <a-col :xl="24" :lg="24" :md="12" :sm="24" :xs="24">
                <bar title="毛利润排行" :dataSource="proData"/>
              </a-col>
              <!-- <a-col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
                <rank-list title="门店销售排行榜" :list="rankList"/>
              </a-col> -->
            </a-row>
          </a-tab-pane>
          <a-tab-pane loading="true" tab="销售额" key="2">
            <a-row>
              <a-col :xl="24" :lg="24" :md="12" :sm="24" :xs="24">
                <bar title="销售额排行" :dataSource="barData"/>
              </a-col>
              <!-- <a-col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
                <rank-list title="门店销售排行榜" :list="rankList"/>
              </a-col> -->
            </a-row>
          </a-tab-pane>

          <!-- <a-tab-pane tab="销售趋势" key="2">
            <a-row>
              <a-col :xl="16" :lg="12" :md="12" :sm="24" :xs="24">
                <bar title="销售额趋势" :dataSource="barData"/>
              </a-col>
              <a-col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
                <rank-list title="门店销售排行榜" :list="rankList"/>
              </a-col>
            </a-row>
          </a-tab-pane> -->
        </a-tabs>
      </div>
    </a-card>

    <!-- <a-row>
      <a-col :span="24">
        <a-card :loading="loading" :bordered="false" title="最近一周访问量统计" :style="{ marginTop: '24px' }">
          <a-row>
            <a-col :span="6">
              <head-info title="今日IP" :content="loginfo.todayIp"></head-info>
            </a-col>
            <a-col :span="2">
              <a-spin class='circle-cust'>
                <a-icon slot="indicator" type="environment" style="font-size: 24px"  />
              </a-spin>
            </a-col>
            <a-col :span="6">
              <head-info title="今日访问" :content="loginfo.todayVisitCount"></head-info>
            </a-col>
            <a-col :span="2">
              <a-spin class='circle-cust'>
                <a-icon slot="indicator" type="team" style="font-size: 24px"  />
              </a-spin>
            </a-col>
            <a-col :span="6">
              <head-info title="总访问量" :content="loginfo.totalVisitCount"></head-info>
            </a-col>
            <a-col :span="2">
              <a-spin class='circle-cust'>
                <a-icon slot="indicator" type="rise" style="font-size: 24px"  />
              </a-spin>
            </a-col>
          </a-row>
          <line-chart-multid :fields="visitFields" :dataSource="visitInfo"></line-chart-multid>
        </a-card>
      </a-col>
    </a-row> -->
  </div>
</template>

<script>
  import ChartCard from '@/components/ChartCard'
  import ACol from "ant-design-vue/es/grid/Col"
  import ATooltip from "ant-design-vue/es/tooltip/Tooltip"
  import MiniArea from '@/components/chart/MiniArea'
  import MiniBar from '@/components/chart/MiniBar'
  import MiniProgress from '@/components/chart/MiniProgress'
  import RankList from '@/components/chart/RankList'
  import Bar from '@/components/chart/Bar'
  import LineChartMultid from '@/components/chart/LineChartMultid'
  import HeadInfo from '@/components/tools/HeadInfo.vue'
  import { httpAction, getAction } from '@/api/manage'
  import Trend from '@/components/Trend'
  import { getLoginfo,getVisitInfo } from '@/api/api'

  const rankList = []
  for (let i = 0; i < 7; i++) {
    rankList.push({
      name: '白鹭岛 ' + (i+1) + ' 号店',
      total: 1234.56 - i * 100
    })
  }
  const barData = []
  const proData = []

  export default {
    name: "IndexChart",
    components: {
      ATooltip,
      ACol,
      ChartCard,
      MiniArea,
      MiniBar,
      MiniProgress,
      RankList,
      Bar,
      Trend,
      LineChartMultid,
      HeadInfo
    },
    data() {
      return {
        url: {
          querySaleInfo: "/purchase/sale/querySaleInfo",
          queryCur12Total:"/purchase/sale/queryCur12Total",
          queryCur12TotalProfit:"/purchase/sale/queryCur12TotalProfit",
          query12:"/purchase/sale/query12",
        },
        saleInfo:{
           dailySales:'',
           dailyProfit:'',
           salesMonth:'',
           profitMonth:''

        },
        loading: true,
        center: null,
        rankList,
        barData,
        proData,
        loginfo:{},
        visitFields:['ip','visit'],
        visitInfo:[],
        indicator: <a-icon type="loading" style="font-size: 24px" spin />
      }
    },
    created() {
      setTimeout(() => {
        this.loading = !this.loading
      }, 1000)
      this.initLogInfo();
      this.querySaleInfo();
      this.querySale();

    },
    methods: {
      async querySale(){
       await this.query12();
      this.queryCur12Total();
      this.queryCur12TotalProfit();

      },
      query12(){
     getAction(this.url.query12).then((res)=>{
           console.info(res);
           let list = res.result;
         })
      },
      //查询最近12个月毛利润
    queryCur12TotalProfit(){
         getAction(this.url.queryCur12TotalProfit).then((res)=>{
           console.info(res);
           let list = res.result;
        for (let i = 0; i < list.length; i += 1) {
         proData.push({
         x: `${list[i].month}月`,
         y: list[i].total
          })
  }
          //  this.saleInfo = res.result;
          });
      },
      //查询最近12个月数据
      queryCur12Total(){
         getAction(this.url.queryCur12Total).then((res)=>{
           console.info(res);
           let list = res.result;
        for (let i = 0; i < list.length; i += 1) {
         barData.push({
         x: `${list[i].month}月`,
         y: list[i].total
          })
  }
          //  this.saleInfo = res.result;
          });
      },
      //查询销售数据
      querySaleInfo(){
          getAction(this.url.querySaleInfo).then((res)=>{
           console.info(res.result);
           if(res.result== null){
            this.saleInfo.dailySales=0,
           this.saleInfo.dailyProfit=0,
           this.saleInfo.salesMonth=0,
           this.saleInfo.profitMonth=0
           }
           else{
             this.saleInfo = res.result;
           }
          });
      },
      initLogInfo () {
        getLoginfo(null).then((res)=>{
          if(res.success){
            Object.keys(res.result).forEach(key=>{
              res.result[key] =res.result[key]+""
            })
            this.loginfo = res.result;
          }
        })
        getVisitInfo().then(res=>{
          if(res.success){
             this.visitInfo = res.result;
           }
         })
      },
    }
  }
</script>

<style lang="less" scoped>
  .circle-cust{
    position: relative;
    top: 28px;
    left: -100%;
  }
  .extra-wrapper {
    line-height: 55px;
    padding-right: 24px;

    .extra-item {
      display: inline-block;
      margin-right: 24px;

      a {
        margin-left: 24px;
      }
    }
  }

  /* 首页访问量统计 */
  .head-info {
    position: relative;
    text-align: left;
    padding: 0 32px 0 0;
    min-width: 125px;

    &.center {
      text-align: center;
      padding: 0 32px;
    }

    span {
      color: rgba(0, 0, 0, .45);
      display: inline-block;
      font-size: .95rem;
      line-height: 42px;
      margin-bottom: 4px;
    }
    p {
      line-height: 42px;
      margin: 0;
      a {
        font-weight: 600;
        font-size: 1rem;
      }
    }
  }
</style>