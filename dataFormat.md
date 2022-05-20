# Data Format
## Part 1
## Part 2
## Part 3
![img.png](./dataFormat.assets/img.png)

### Left 

<img src="./dataFormat.assets/image-20220520214103947.png" alt="image-20220520214103947" style="zoom:50%;" />

**data format**

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

**data format**

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

