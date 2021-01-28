<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
                 <!--  字典下拉 -->
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="性别">
              <j-dict-select-tag v-model="formData1.sex" title="性别" dictCode="sex" placeholder="请选择性别"/>
              <!--  <j-dict-select-tag title="性别" dictCode="sex" disabled/>-->
            </a-form-item>
          </a-col>
          <a-col :span="12">选中值：{{ formData1.sex}}</a-col>
        </a-row>
        <a-row>
          <a-col :span="24">
            <a-form-item label="货品名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['name']" placeholder="请输入货品名称"  ></a-input>
            </a-form-item>
          </a-col>

       
          <a-col :span="24">
            <a-form-item label="单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['company']" placeholder="请输入单位"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="单价" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['unitPrice']" placeholder="请输入单价" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['account']" placeholder="请输入数量" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="总价" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['totalPrice']" placeholder="请输入总价" style="width: 100%" />
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
  import SelectDemo from '@/views/jeecg/SelectDemo'
  export default {
    name: 'GoodsUseRecordForm',
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
        formData1: {
          areaLinkage1: '110105',
          areaLinkage2: '140221',
          sex: 1
        },
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
          add: "/purchase/goodsUseRecord/add",
          edit: "/purchase/goodsUseRecord/edit",
          queryById: "/purchase/goodsUseRecord/queryById"
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
    },
    methods: {
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
        this.form.setFieldsValue(pick(row,'name','company','unitPrice','account','totalPrice'))
      },
    }
  }
</script>