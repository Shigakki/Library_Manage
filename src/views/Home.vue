<template>
  <div>
    <el-row :gutter=10 style="margin-bottom: 40px">
      <el-col :span="6">
        <el-card>
          <div style="color: #409EFF"><i class="el-icon-user-solid"></i>用户总数</div>
          <div style="padding:10px 0; text-align: center; font-weight: bold">
            100
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div style="color: #67C23A">用户总数</div>
          <div style="padding:10px 0; text-align: center; font-weight: bold">
            100
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div style="color: #E6A23C">用户总数</div>
          <div style="padding:10px 0; text-align: center; font-weight: bold">
            100
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #F56C6C">
          <div >用户总数</div>
          <div style="padding:10px 0; text-align: center; font-weight: bold">
            100
          </div>
        </el-card>
      </el-col>

    </el-row>

    <el-row>
      <el-col :span="12">
        <div id="main" style="width: 500px;height: 400px"></div>
      </el-col>

      <el-col :span="12">
        <div id="pie_chart" style="width: 500px;height: 400px"></div>
      </el-col>

    </el-row>


  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: "Home",
  data() {
    return {}
  },
  mounted() { //页面div元素渲染完才会出发mounted
    var option;
    option = {
      title: {
        text: '各季度用户注册数量统计',
        subtext: '趋势图',
        left: 'center'
      },
      xAxis: {
        type: 'category',
        data: ['第一季度', '第二季度', '第三季度', '第四季度']
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          data: [150, 230, 224, 218, 135, 147, 260],
          type: 'line'
        },
        {
          data: [150, 230, 224, 218, 135, 147, 260],
          type: 'bar'
        }
      ]
    };
    var chartDom = document.getElementById('main');
    var myChart = echarts.init(chartDom);

    var pieCharOption;
    pieCharOption = {
      title: {
        text: '各季度用户注册数量统计',
        subtext: '比例图',
        left: 'center'
      },
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      series: [
        {
          name: 'Access From',
          type: 'pie',
          radius: '50%',
          label: {
            show: true,
            position: 'inner',
            textStyle: {
              fontWeight: 300,
              fontSize: 16,
              color: '#fff'
            },
            formatter: '{d}%'
          },
          data: [
            {value: 1048, name: 'Search Engine'},
            {value: 735, name: 'Direct'},
            {value: 580, name: 'Email'},
            {value: 484, name: 'Union Ads'},
            {value: 300, name: 'Video Ads'}
          ],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    };
    var pieChartDom = document.getElementById('pie_chart');
    var pieChart = echarts.init(pieChartDom);
    pieChart.setOption(pieCharOption)

    this.request.get('/echarts/member')
        .then(res => {
          option.series[0].data = res.data
          option.series[1].data = res.data
          myChart.setOption(option);

          pieCharOption.series[0].data = [
            {name: "第一季度", value: res.data[0]},
            {name: "第二季度", value: res.data[1]},
            {name: "第三季度", value: res.data[2]},
            {name: "第四季度", value: res.data[3]},
          ]
          pieChart.setOption(pieCharOption)
        })
  }
}

</script>

<style scoped>

</style>