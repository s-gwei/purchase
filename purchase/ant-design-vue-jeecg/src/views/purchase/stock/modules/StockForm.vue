<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        
        <a-row>
          <a-col :md="14" :sm="16">
           <a-form-item label="货品名称"  style="display:flex;position:relative;left:90px">
                    <a-select style="width:350px" name="projectNameList" v-decorator="['id']" placeholder="请选择货品名称" @change="selectOrderPro()">
                        <a-select-option value="">请选择</a-select-option>
                        <a-select-option v-for="item in orderProList" :key="item.id" :value="item.id" >{{item.name}},单价:{{item.unitPrice}}/{{item.company}}</a-select-option>
                    </a-select>
            </a-form-item>
              </a-col> 
           <!-- <a-col :span="24" v-show=false>
            <a-form-item label="id" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['id']" placeholder="请输入货品id"  ></a-input>
            </a-form-item>
          </a-col>   -->
          <!-- <a-col :span="24">
            <a-form-item label="单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['company']" placeholder="请输入单位"  ></a-input>
            </a-form-item>
          </a-col> -->
          <!-- <a-col :span="24">
            <a-form-item label="单价" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['unitPrice']" placeholder="请输入单价" style="width: 100%" />
            </a-form-item>
          </a-col> -->
          <a-col :span="24">
            <a-form-item label="使用数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['account']" placeholder="请输入使用数量" style="width: 100%" />
            </a-form-item>
          </a-col>
          <!-- <a-col :span="24">
            <a-form-item label="总价" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['totalPrice']" placeholder="请输入总价" style="width: 100%"/>
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
  import { validateDuplicateValue } from '@/utils/util'
  import JFormContainer from '@/components/jeecg/JFormContainer'

  export default {
    name: 'StockForm',
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
        form: this.$form.createForm(this),
        model: {},
        orderProList:[],
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
          add: "/purchase/stock/add",
          edit: "/purchase/stock/edit",
          queryById: "/purchase/stock/queryById",
          queryOrderList:"/purchase/stock/list",
          reduce:"/purchase/stock/reduce"
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
      this.queryOrderList();
    },
    methods: {
      //获取选中原料
      selectOrderPro(){
        console.info(this.form.getFieldValue('name'))
      },
      queryOrderList(){
           getAction(this.url.queryOrderList).then((res)=>{
             this.orderProList = res.result.records;
              console.info(res);
            console.info(this.orderProList);
          });
      },
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'name','company','unitPrice','account','totalPrice'))
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
        console.info(this.form.getFieldValue('id'))
        if(this.form.getFieldValue('id') == undefined || this.form.getFieldValue('id')==null){
           that.$message.warning("请选择货品");
           return;
        }
        if(this.form.getFieldValue('account') == undefined || this.form.getFieldValue('account')==null){
               that.$message.warning("请输入使用数量");
           return;
        }
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.reduce;
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
        this.form.setFieldsValue(pick(row,'name','company','unitPrice','account','totalPrice'))
      },
    }
  }
</script>