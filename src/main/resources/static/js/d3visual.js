// let data = [[${data}]];
// console.log(data);
function getTextWidth(text, fontSize, fontname) {
  c = document.createElement("canvas");
  ctx = c.getContext("2d");
  ctx.font = fontSize + " " + fontname;
  return ctx.measureText(text).width;
}

function DataSegregator(array, on) {
  var SegData;
  OrdinalPositionHolder = {
    valueOf: function () {
      thisObject = this;
      keys = Object.keys(thisObject);
      keys.splice(keys.indexOf("valueOf"), 1);
      keys.splice(keys.indexOf("keys"), 1);
      return keys.length == 0
        ? -1
        : d3.max(keys, function (d) {
            return thisObject[d];
          });
    },
    keys: function () {
      keys = Object.keys(thisObject);
      keys.splice(keys.indexOf("valueOf"), 1);
      keys.splice(keys.indexOf("keys"), 1);
      return keys;
    },
  };
  array[0]
    .map(function (d) {
      return d[on];
    })
    .forEach(function (b) {
      value = OrdinalPositionHolder.valueOf();
      OrdinalPositionHolder[b] = OrdinalPositionHolder > -1 ? ++value : 0;
    });

  SegData = OrdinalPositionHolder.keys().map(function () {
    return [];
  });

  array.forEach(function (d) {
    d.forEach(function (b) {
      SegData[OrdinalPositionHolder[b[on]]].push(b);
    });
  });

  return SegData;
}

// Data = [
//   {
//     date: "2016-06-14",
//     categories: [
//       { name: "Độ ẩm", value: 368 },
//       { name: "PPM", value: 321 },
//     ],
//     lineCategory: [
//       { name: "Nhiệt độ", value: 69 },
//       { name: "Line2", value: 63 },
//     ],
//   },
//   {
//     date: "2016-06-15",
//     categories: [
//       { name: "Độ ẩm", value: 521 },
//       { name: "PPM", value: 123 },
//     ],
//     lineCategory: [
//       { name: "Nhiệt độ", value: 89 },
//       { name: "Line2", value: 96 },
//     ],
//   },
//   {
//     date: "2016-06-17",
//     categories: [
//       { name: "Độ ẩm", value: 368 },
//       { name: "PPM", value: 236 },
//     ],
//     lineCategory: [
//       { name: "Nhiệt độ", value: 63 },
//       { name: "Line2", value: 35 },
//     ],
//   },
//   {
//     date: "2016-06-18",
//     categories: [
//       { name: "Độ ẩm", value: 423 },
//       { name: "PPM", value: 330 },
//     ],
//     lineCategory: [
//       { name: "Nhiệt độ", value: 86 },
//       { name: "Line2", value: 45 },
//     ],
//   },
//   {
//     date: "2016-06-19",
//     categories: [
//       { name: "Độ ẩm", value: 601 },
//       { name: "PPM", value: 423 },
//     ],
//     lineCategory: [
//       { name: "Nhiệt độ", value: 65 },
//       { name: "Line2", value: 63 },
//     ],
//   },
//   {
//     date: "2016-06-20",
//     categories: [
//       { name: "Độ ẩm", value: 412 },
//       { name: "PPM", value: 461 },
//     ],
//     lineCategory: [
//       { name: "Nhiệt độ", value: 75 },
//       { name: "Line2", value: 85 },
//     ],
//   },
// ];

var margin = { top: 20, right: 30, bottom: 60, left: 40 },
  width = 700 - margin.left - margin.right,
  height = 500 - margin.top - margin.bottom;

var textWidthHolder = 0;
/// Adding date in lineCategory
Data.forEach(function (d) {
  d.lineCategory.forEach(function (b) {
    b.date = d.date;
  });
});

var categories = new Array();
// Extension method declaration

categories.pro;

var agenames;
var x0 = d3.scale.ordinal().rangeRoundBands([0, width], 0.1);
var XLine = d3.scale.ordinal().rangeRoundPoints([0, width], 0.1);
var x1 = d3.scale.ordinal();

var y = d3.scale.linear().range([height, 0]);

var YLine = d3.scale
  .linear()
  .range([height, 0])
  .domain([
    0,
    d3.max(Data, function (d) {
      return d3.max(d.lineCategory, function (b) {
        return b.value;
      });
    }),
  ]);

var color = d3.scale
  .ordinal()
  .range([
    "#98abc5",
    "#8a89a6",
    "#7b6888",
    "#6b486b",
    "#a05d56",
    "#d0743c",
    "#ff8c00",
  ]);

var line = d3.svg
  .line()
  .x(function (d) {
    return x0(d.date) + x0.rangeBand() / 2;
  })
  .y(function (d) {
    return YLine(d.value);
  });

var xAxis = d3.svg.axis().scale(x0).orient("bottom");

var yAxis = d3.svg.axis().scale(y).orient("left").tickFormat(d3.format(".2s"));

var YLeftAxis = d3.svg
  .axis()
  .scale(YLine)
  .orient("right")
  .tickFormat(d3.format(".2s"));

var svg = d3
  .select("#my_dataviz")
  .append("svg")
  .attr("width", width + margin.left + margin.right)
  .attr("height", height + margin.top + margin.bottom)
  .append("g")
  .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

// Bar Data categories
Data.forEach(function (d) {
  d.categories.forEach(function (b) {
    if (
      categories.findIndex(function (c) {
        return c.name === b.name;
      }) == -1
    ) {
      b.Type = "bar";
      console.log(JSON.stringify(b));
      categories.push(b);
    }
  });
});

// Line Data categories
Data.forEach(function (d) {
  d.lineCategory.forEach(function (b) {
    if (
      categories.findIndex(function (c) {
        return c.name === b.name;
      }) == -1
    ) {
      b.Type = "line";
      console.log(JSON.stringify(b));
      categories.push(b);
    }
  });
});

// Processing Line data
lineData = DataSegregator(
  Data.map(function (d) {
    return d.lineCategory;
  }),
  "name"
);

// Line Coloring
LineColor = d3.scale.ordinal();
LineColor.domain(
  categories
    .filter(function (d) {
      return d.Type == "line";
    })
    .map(function (d) {
      return d.name;
    })
);
LineColor.range(["#d40606", "#06bf00", "#98bdc5", "#671919", "#0b172b"]);
x0.domain(
  Data.map(function (d) {
    return d.date;
  })
);
XLine.domain(
  Data.map(function (d) {
    return d.date;
  })
);
x1.domain(
  categories
    .filter(function (d) {
      return d.Type == "bar";
    })
    .map(function (d) {
      return d.name;
    })
).rangeRoundBands([0, x0.rangeBand()]);
y.domain([
  0,
  d3.max(Data, function (d) {
    return d3.max(d.categories, function (d) {
      return d.value;
    });
  }),
]);

svg
  .append("g")
  .attr("class", "x axis")
  .attr("transform", "translate(0," + height + ")")
  .call(xAxis);

svg
  .append("g")
  .attr("class", "y axis")
  .attr("transform", "translate(" + width + ",0)")
  .call(YLeftAxis)

  .append("text")
  .attr("transform", "rotate(-90)")
  .attr("y", -10)

  .attr("dy", ".71em")
  .style("text-anchor", "end")
  .text("độ C");

svg
  .append("g")
  .attr("class", "y axis")
  .call(yAxis)
  .append("text")
  .attr("transform", "rotate(-90)")
  .attr("y", 1)
  .attr("dy", ".71em")
  .style("text-anchor", "end")
  .text("");

var state = svg
  .selectAll(".state")
  .data(Data)
  .enter()
  .append("g")
  .attr("class", "state")
  .attr("transform", function (d) {
    return "translate(" + x0(d.date) + ",0)";
  });

state
  .selectAll("rect")
  .data(function (d) {
    return d.categories;
  })
  .enter()
  .append("rect")
  .attr("width", x1.rangeBand())
  .attr("x", function (d) {
    return x1(d.name);
  })
  .attr("y", function (d) {
    return y(d.value);
  })
  //.attr("height", function (d) { return height - y(d.value); })
  .style("fill", function (d) {
    return color(d.name);
  })
  .transition()
  .delay(500)
  .attrTween("height", function (d) {
    var i = d3.interpolate(0, height - y(d.value));
    return function (t) {
      return i(t);
    };
  });

// drawaing lines
svg
  .selectAll(".lines")
  .data(lineData)
  .enter()
  .append("g")
  .attr("class", "line")
  .each(function (d) {
    name = d[0].name;
    d3.select(this)
      .append("path")
      .attr("d", function (b) {
        return line(b);
      })
      .style({ "stroke-width": "2px", fill: "none" })
      .style("stroke", LineColor(name))
      .transition()
      .duration(1500);
  });

// Legends

var LegendHolder = svg.append("g").attr("class", "legendHolder");
var legend = LegendHolder.selectAll(".legend")
  .data(
    categories.map(function (d) {
      return { name: d.name, Type: d.Type };
    })
  )
  .enter()
  .append("g")
  .attr("class", "legend")
  .attr("transform", function (d, i) {
    return "translate(0," + (height + margin.bottom / 2) + ")";
  })
  .each(function (d, i) {
    //  Legend Symbols

    d3.select(this)
      .append("rect")
      .attr("width", function () {
        return 18;
      })
      .attr("x", function (b) {
        left = (i + 1) * 15 + i * 18 + i * 5 + textWidthHolder;
        return left;
      })
      .attr("y", function (b) {
        return b.Type == "bar" ? 0 : 7;
      })
      .attr("height", function (b) {
        return b.Type == "bar" ? 18 : 5;
      })
      .style("fill", function (b) {
        return b.Type == "bar" ? color(d.name) : LineColor(d.name);
      });

    //  Legend Text

    d3.select(this)
      .append("text")
      .attr("x", function (b) {
        left = (i + 1) * 15 + (i + 1) * 18 + (i + 1) * 5 + textWidthHolder;

        return left;
      })
      .attr("y", 9)
      .attr("dy", ".35em")
      .style("text-anchor", "start")
      .text(d.name);

    textWidthHolder += getTextWidth(d.name, "10px", "calibri");
  });

// Legend Placing

d3.select(".legendHolder").attr("transform", function (d) {
  thisWidth = d3.select(this).node().getBBox().width;
  return "translate(" + (width / 2 - thisWidth / 2) + ",0)";
});
