# Data Format
## Part 1

> 这一页都是用已经爬好的数据



<img src="./dataFormat.assets/image-20220520215744957.png" alt="img.png" style="zoom: 33%;" /> and <img src="./dataFormat.assets/image-20220520215826959.png" alt="img.png" style="zoom: 33%;" />



#### :star: Corresponding data

###### LanguageTotalRepoByTime.json

> LanguageTotalIssueByTime.json and LanguageTotalUserByTime.json are in the same format

```json
{
    2007: [ // a year's data
        {
            count: 10, // current year's increasement
            name: "Java", // lanuage's name
            value: 10 // accumulated number up to current year
        }
    ],
    2008: [
        {
            count: 12,
            name: "Java",
            value: 22
        }
    ]
}
```

### Graph

#### Stacked Line

![image-20220520221917311](dataFormat.assets/image-20220520221917311.png)

展示每一年前7的语言排名，y轴是排名从上到下数字越大，最上面数字为1；x轴是年份，展示2012～2021年

> 用总仓库数来排名，LanguageTotalRepoByTime.json 里面的value

```json
{
    legend: ['Email', 'Union Ads', 'Video Ads', 'Direct', 'Search Engine'], // 要改成所有的语言名
    xAxis: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'], //改成年份
    series: [
        {
            name: 'Email',
            data: [120, 132, 101, 134, 90, 230, 210] // 要和xAxis对应， 注意应该是排名不是仓库数量
          },
          {
            name: 'Union Ads',
            data: [ ,  , 191, 234, 290, 330, 310] // 如果某个语言在某一年不是前七，那一年不用写数据
          },
        ...
    ]
}
```
#### Pie graph at right-bottom corner

![image-20220520223933733](dataFormat.assets/image-20220520223933733.png)

和上一个图是对应的，当鼠标移动到上一个图的x轴某一点，该饼图会展示对应年份前7的语言的总仓库数

```json
{
    "2007": [ // 每一年只要前7的语言
        {value: 1048, name: 'Search Engine'}, // name 是语言名， value是该年的该语言的总仓库数
        {value: 735, name: 'Direct'},
        {value: 580, name: 'Email'},
        {value: 484, name: 'Union Ads'},
        {value: 300, name: 'Video Ads'}
    ],
    "2008": [
        {value: 1048, name: 'Search Engine'},
        {value: 735, name: 'Direct'},
        {value: 580, name: 'Email'},
        {value: 484, name: 'Union Ads'},
        {value: 300, name: 'Video Ads'}
    ]
}
```



#### Rank by time

<img src="dataFormat.assets/image-20220520222306755.png" alt="image-20220520222306755" style="zoom: 67%;" />

动态展示每一年不同热门语言仓库数量增量

> 用LanguageTotalRepoByTime.json 的 count

```json
[
    {
        language:[
            "Java",
            "C"
        ],
        nums:[
            126967, // 用LanguageTotalRepoByTime.json 的count
            10000
        ],
        year: 2012
    },
    {
        language:[
            "Java",
            "C"
        ],
        nums:[
            126967,
            10000
        ],
        year: 2013
    }
]
```

#### 最后一个图

<img src="dataFormat.assets/image-20220520230615161.png" alt="image-20220520230615161" style="zoom:50%;" />


## Part 2
## Part 3
![img.png](./dataFormat.assets/img.png)

### Left 

<img src="./dataFormat.assets/image-20220520214103947.png" alt="image-20220520214103947" style="zoom:50%;" />

展示该仓库的贡献者数量，issue的平均评论数，pr的平均评论数

```js
// value: (you know)
// name: should be "Contributors", "Issue comments (avg)", "PR comments (avg)"
[
  {
    value: 20,
    name: 'Perfect'
  },
  {
    value: 40,
    name: 'Good'
  },
  {
    value: 100,
    name: 'Commonly'
  }
]
```

#### Right

<img src="./dataFormat.assets/image-20220520215047952.png" alt="image-20220520215047952" style="zoom: 80%;" /> or <img src="/home/zrh/Repository/gitrepo/CS209A_Project2/dataFormat.assets/image-20220520215147974.png" alt="image-20220520215147974" style="zoom: 50%;" />   

展示程序猿都喜欢在什么时间工作（就是commit时间）

```js
// hours: x axis, string
// days: y axis
// data: [x, y, value], [0, 0, 5] means ('12a', 'Saturday')'s value is 5
{
    hours: [
        '12a', '1a', '2a', '3a', '4a', '5a', '6a',
        '7a', '8a', '9a', '10a', '11a',
        '12p', '1p', '2p', '3p', '4p', '5p',
        '6p', '7p', '8p', '9p', '10p', '11p'
	],
	days: [
        'Sat', 'Fri', 'Thu',
        'Wed', 'Tus', 'Mon', 'Sun'
	],
	data: [[0, 0, 5], [0, 1, 1], [0, 2, 0], [0, 3, 0]]
}
```

