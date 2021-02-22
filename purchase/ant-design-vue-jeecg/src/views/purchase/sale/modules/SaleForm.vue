<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row class="row-set" style="width: 100%">
          <div class="div-set" style="width: 100%">
            <a-col :md="14" :sm="16">
              <a-form-item class="form-item" label="客户店名" style="display: flex; position: relative; left: 117px">
                <a-select
                  style="width: 350px"
                  name="projectNameList"
                  v-decorator="['customerId']"
                  placeholder="请选择客户"
                  @change = "queryProList()"
                >
                  <a-select-option value="">请选择</a-select-option>
                  <a-select-option v-for="item in couList" :key="item.id" :value="item.id">{{
                    item.shopName
                  }}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </div>
          <div style="align: auto">
            <a-col :md="14" :sm="16">
              <a-form-item label="产品名称" style="display: flex; position: relative; left: 117px">
                <a-select
                  style="width: 350px"
                  name="projectNameList"
                  v-decorator="['productId']"
                  placeholder="请选择产品"
                    @blur = "queryPrice()"
                >
                  <a-select-option value="">请选择</a-select-option>
                  <a-select-option v-for="item in proList" :key="item.orderId" :value="item.orderId">{{
                    item.orderName
                  }}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </div>
          <!-- <a-col :span="24">
            <a-form-item label="产品名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['productName']" placeholder="请输入产品名称"  ></a-input>
            </a-form-item>
          </a-col> -->
          <!-- <a-col :span="24">
            <a-form-item label="产品id" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['productId']" placeholder="请输入产品id"  ></a-input>
            </a-form-item>
          </a-col> -->
          <!-- <a-col :span="24">
            <a-form-item label="单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['company']" placeholder="请输入单位"  ></a-input>
            </a-form-item> -->
          <!-- </a-col> -->
          <a-col :span="24">
            <a-form-item label="单价" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['unitPrice']" placeholder="请输入单价" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['account']" placeholder="请输入数量" style="width: 100%" @blur="calcul()" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="总价" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['totalPrice']" placeholder="请输入总价" style="width: 100%" />
            </a-form-item>
          </a-col>
           <a-col :span="24">
                     <a-form-item label="订单日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-date-picker
            style="width: 100%"
            placeholder="请选择日期"
            v-decorator="['createTime', {initialValue:!model.createTime?null:moment(model.createTime,dateFormat)}]"
            :getCalendarContainer="node => node.parentNode"/>
        </a-form-item>
          </a-col> 
          <!-- <a-col :span="24">
            <a-form-item label="客户id" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['customerId']" placeholder="请输入客户id"  ></a-input>
            </a-form-item>
          </a-col> -->
          <!-- <a-col :span="24">
            <a-form-item label="客户店名" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['customerName']" placeholder="请输入客户店名"  ></a-input>
            </a-form-item>
          </a-col> -->
          
          <a-col v-if="showFlowSubmitButton" :span="24" style="text-align: center">
            <a-button @click="submitForm">提 交</a-button>
          </a-col>
        </a-row>
      </a-form>
    </j-form-container>
  </a-spin>
</template>

<script>
import { httpAction, getAction } from '@/api/manage'
import pick from 'lodash.pick'
import JFormContainer from '@/components/jeecg/JFormContainer'
import moment from 'moment'


export default {
  name: 'SaleForm',
  components: {
    JFormContainer,
  },
  props: {
    //流程表单data
    formData: {
      type: Object,
      default: () => {},
      required: false,
    },
    //表单模式：true流程表单 false普通表单
    formBpm: {
      type: Boolean,
      default: false,
      required: false,
    },
    //表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false,
    },
  },
  data() {
    return {
      dateFormat:"YYYY-MM-DD HH:mm:ss",
      form: this.$form.createForm(this),
      couList: [],
      proList: [],
      model: {},
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      confirmLoading: false,
      validatorRules: {},
      url: {
        add: '/purchase/sale/add',
        edit: '/purchase/sale/edit',
        queryById: '/purchase/sale/queryById',
        queryCouList: '/purchase/customer/queryList',
        queryProList: '/purchase/consumptOrder/listAll',
      },
    }
  },
  computed: {
    formDisabled() {
      if (this.formBpm === true) {
        if (this.formData.disabled === false) {
          return false
        }
        return true
      }
      return this.disabled
    },
    showFlowSubmitButton() {
      if (this.formBpm === true) {
        if (this.formData.disabled === false) {
          return true
        }
      }
      return false
    },
  },
  created() {
    //如果是流程中表单，则需要加载流程表单data
    this.showFlowData()
    this.queryCouList()
    // this.queryProList()
  },
  methods: {
    queryPrice(){
        // console.info(this.form.getFieldValue('productId'))
        let productIds = this.form.getFieldValue('productId')
        console.info(productIds)
        let product = {};
        for(var i=0;i< this.proList.length;i++){
          // console.info(this.proList[i]["orderId"] )
          if( this.proList[i]["orderId"] ==productIds ){
             product = this.proList[i]
          }
        }
         console.info(product);
          let record = []
          record.unitPrice = product["price"];
          this.model = Object.assign({}, record)
       this.form.setFieldsValue(pick(this.model, 'unitPrice'))
    },
    //计算总价
    calcul() {
      let unitPrice = this.form.getFieldValue('unitPrice')
      console.info(unitPrice)
      let account = this.form.getFieldValue('account')
      console.info(account)
      if (unitPrice == null || account == null) {
        that.$message.warning('请选择单价和数量')
        return
      }
      let record = []
      record.totalPrice = this.NumberMul(unitPrice,account)
      console.info(record.totalPrice)
      this.model = Object.assign({}, record)
      this.form.setFieldsValue(pick(this.model, 'totalPrice'))
    },

     NumberMul(arg1, arg2) {
    var m = 0;
    var s1 = arg1.toString();
    var s2 = arg2.toString();
    try {
        m += s1.split(".")[1].length;
    } catch (e) {}
    try {
        m += s2.split(".")[1].length;
    } catch (e) {}
 
    return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m);
},
    //查询产品信息
    queryProList() {
      getAction(this.url.queryProList).then((res) => {
        console.info(res)
        this.proList = res.result
        console.info(this.proList)
      })
    },
    //查询客户信息
    queryCouList() {
      getAction(this.url.queryCouList).then((res) => {
        // console.info(res)
        this.couList = res.result
        // console.info(this.couList)
      })
    },
    add() {
      this.edit({})
    },
    edit(record) {
      this.form.resetFields()
      this.model = Object.assign({}, record)
      this.visible = true
      this.$nextTick(() => {
        this.form.setFieldsValue(
          pick(
            this.model,
            'productName',
            'productId',
            'company',
            'unitPrice',
            'account',
            'totalPrice',
            'customerId',
            'customerName',
            'createTime'
          )
        )
      })
    },
    //渲染流程表单数据
    showFlowData() {
      if (this.formBpm === true) {
        let params = { id: this.formData.dataId }
        getAction(this.url.queryById, params).then((res) => {
          if (res.success) {
            this.edit(res.result)
          }
        })
      }
    },
    moment,
    submitForm() {
      const that = this
      // 触发表单验证
      this.form.validateFields((err, values) => {
        if (!err) {
          that.confirmLoading = true
          let httpurl = ''
          let method = ''
          if (!this.model.id) {
            httpurl += this.url.add
            method = 'post'
          } else {
            httpurl += this.url.edit
            method = 'put'
          }
           if(!values.createTime){
              values.createTime = '';
            }else{
              values.createTime = values.createTime.format(this.dateFormat);
            }
           console.log('values', values)
          let formData = Object.assign(this.model, values)
          console.log('表单提交数据', formData)
          httpAction(httpurl, formData, method)
            .then((res) => {
              if (res.success) {
                that.$message.success(res.message)
                that.$emit('ok')
              } else {
                that.$message.warning(res.message)
              }
            })
            .finally(() => {
              that.confirmLoading = false
            })
        }
      })
    },
    popupCallback(row) {
      this.form.setFieldsValue(
        pick(
          row,
          'productName',
          'productId',
          'company',
          'unitPrice',
          'account',
          'totalPrice',
          'customerId',
          'customerName'
        )
      )
    },
  },
}
</script>