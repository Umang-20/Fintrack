import { Chart } from "chart.js/auto";
import React from "react";
import { Doughnut } from "react-chartjs-2";
import ChartDataLabels from "chartjs-plugin-datalabels";

const ChartModel = ({ data, label }) => {
  Chart.register(ChartDataLabels);

  const options = {
    plugins: {
      colors: {
        forceOverride: true,
      },
      legend: {
        display: false,
      },
      datalabels: {
        formatter: (value, ctx) => {
          const datapoints = ctx.chart.data.datasets[0].data;
          const total = datapoints.reduce((total, datapoint) => total + datapoint, 0);
          const percentage = (value / total) * 100;
          return percentage.toFixed(2) + "%";
        },
        color: "#000",
      },
      tooltip: {
        enabled: true,
        usePointStyle: true,
        callbacks: {
          label: (tooltipItems) => tooltipItems.label + " ($" + tooltipItems.formattedValue + ")",
          title: () => null,
        },
      },
    },
  };
  return (
    <div className="d-flex flex-column">
      {data && <Doughnut data={data} options={options} />}
      <div className="mt-2">{data ? label : `No ${label} data`}</div>
    </div>
  );
};

export default ChartModel;
