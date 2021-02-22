<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
               <div class="div-set" style="width: 100%">
            <a-col :md="14" :sm="16">
              <a-form-item class="form-item" label="客户店名" style="display: flex; position: relative; left: 117px">
                <a-select
                  style="width: 350px"
                  name="projectNameList"
                  v-decorator="['coumsterId']"
                  placeholder="请选择客户"
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
                  v-decorator="['orderId']"
                  placeholder="请选择产品"
                >
                  <a-select-option value="">请选择</a-select-option>
                  <a-select-option v-for="item in proList" :key="item.id" :value="item.id">{{
                    item.name
                  }}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </div>
          <a-col :span="24">
            <a-form-item label="价格(元)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['price']" placeholder="请输入价格" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="单价" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['company']" placeholder="请输入单价"  ></a-input>
            </a-form-item>
          </a-col>
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
  import { validateDuplicateValue } from '@/utils/util'
  import JFormContainer from '@/components/jeecg/JFormContainer'

  export default {
    name: 'ConsumptOrderForm',
    components: {
      JFormContainer,
    },
    props: {
      //流程表单data
      formData: {
        type: Object,
        default: ()=>{},
        required: false
      },
      //表单模式：true流程表单 false普通表单
      formBpm: {
        type: Boolean,
        default: false,
        required: false
      },
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
        couList: [],
        proList: [],
        form: this.$form.createForm(this),
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
        validatorRules: {
        },
        url: {
          add: "/purchase/consumptOrder/add",
          edit: "/purchase/consumptOrder/edit",
          queryById: "/purchase/consumptOrder/queryById",
          queryCouList: '/purchase/customer/queryList',
          queryProList: '/purchase/product/queryList',
        }
      }
    },
    computed: {
      formDisabled(){
        if(this.formBpm===true){
          if(this.formData.disabled===false){
            return false
          }
          return true
        }
        return this.disabled
      },
      showFlowSubmitButton(){
        if(this.formBpm===true){
          if(this.formData.disabled===false){
            return true
          }
        }
        return false
      }
    },
    created () {
      //如果是流程中表单，则需要加载流程表单data
      this.showFlowData();
      this.queryCouList()
      this.queryProList()
    },
    methods: {
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
        console.info(res)
        this.couList = res.result
        console.info(this.couList)
      })
    },
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'coumsterId','coumsterName','orderId','orderName','price','company'))
        })
      },
      //渲染流程表单数据
      showFlowData(){
        if(this.formBpm === true){
          let params = {id:this.formData.dataId};
          getAction(this.url.queryById,params).then((res)=>{
            if(res.success){
              this.edit (res.result);
            }
          });
        }
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            let formData = Object.assign(this.model, values);
            console.log("表单提交数据",formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          }
         
        })
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'coumsterId','coumsterName','orderId','orderName','price','company'))
      },
    }
  }
</script>