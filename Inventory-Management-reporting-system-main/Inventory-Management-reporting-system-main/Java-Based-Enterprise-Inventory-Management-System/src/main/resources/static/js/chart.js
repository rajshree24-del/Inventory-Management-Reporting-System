window.onload=function(){

const ctx=document.getElementById('stockChart');

new Chart(ctx,{
type:'line',

data:{
labels:["M","T","W","T","F","S","S"],

datasets:[{
label:'Stock Trend',
data:[30,40,35,50,49,60,55],
borderColor:'#10b981',
fill:false,
tension:0.4
}]
}

});

}