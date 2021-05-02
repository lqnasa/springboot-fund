# 天天基金持仓采集，股票机构调用次数采集
```text
    实现采集股票型基金持仓top20的股票数据。
    
    目的：可以根据股票数据来分析股票是否可持有。（当然公布有滞后性。）
    理论上，越多基金公司持有，且可以通过每个季度的基金持仓是否变化来看，查看该股是否热度，基金是否大量买入持仓。
    来挑选出好股票，或者可以根据好的股票优选基金。

    增加：采集股票被机构调研次数的数据。引入redis去重。

    仅供参考

```

```postgresql
-- 优选股票
-- 计算出所有基金持仓该股这个季度比上个季度钱数多两倍，剔除科创板
-- 持仓最少大于1亿，剔除港股
-- 同时还要根据所选出的股票，在根据股票走势来看，是否基金在后面一个月有坚持，或者还持有的迹象。（量能，走势是否向上，是否有出货嫌疑，在优选股票）
-- 这个指标数据并不美，还可以在研究。

SELECT
t.stock_name,
t.stock_code,
t.up_until_date,
count( * ) total,
sum( t.hold_money ) money（万） 
FROM
	t_fund_archives_stock t 
WHERE
	t.stock_code IN (
SELECT DISTINCT
	t11.stock_code 
FROM
	(
SELECT
	t1.stock_code,
	sum( t1.hold_money ) money 
FROM
	t_fund_archives_stock t1 
WHERE
	t1.up_until_date = '2021-03-31' 
GROUP BY
	t1.stock_code 
	) t11,
	(
SELECT
	t2.stock_code,
	sum( t2.hold_money ) money 
FROM
	t_fund_archives_stock t2 
WHERE
	t2.up_until_date = '2020-12-31' 
GROUP BY
	t2.stock_code 
	) t22 
WHERE
	t11.stock_code = t22.stock_code 
	AND t11.money > t22.money * 2
	and t11.money > 10000
	and t11.stock_code not like '68%'
	and length(t11.stock_code) > 5
	) 
	and t.up_until_date in ('2020-12-31','2021-03-31')
GROUP BY
	t.up_until_date,
	t.stock_code,
	t.stock_name 
ORDER BY
	t.stock_code,
	t.up_until_date DESC;
```

```postgresql
-- 优选基金
-- 计算出持仓股票存在4只精选票的，且总20只的持仓占比>55
-- 计算出持仓股票存在4只精选票的，且总20只的持仓占比>55
SELECT
	* 
FROM
	t_fund_archives_stock s 
WHERE
	s.fund_code IN (
SELECT
	T.fund_code 
FROM
	t_fund_archives_stock T 
WHERE
	T.up_until_date = '2021-03-31' 
	AND T.stock_name IN (-- 锂电 新能源车
	'赣锋锂业',
	'天齐锂业',
	'宁德时代',
	'比亚迪',
	'天赐材料',-- 医美
	'朗姿股份',
	'贝泰妮',
	'爱美客',
	'华熙生物',-- 钛业
	'龙蟒佰利',
	'中核钛白',-- 芯片
	'韦尔股份',
	'紫光国微',
	'卓胜微',
	'汇顶科技',
	'兆易创新',
	'闻泰科技',
	'歌尔股份',
	'士兰微',
	'中芯国际',
	'国科微',-- 医药
	'智飞生物',
	'迈瑞医疗',
	'万泰生物',
	'恒瑞医药',
	'药明康德',
	'爱尔眼科',-- 光伏
	'隆基股份',
	'阳光电源',
	'通威股份',
	'中环科技' 
	) 
GROUP BY
	T.fund_code 
HAVING
	COUNT( * ) = 4 
	) 
	AND s.up_until_date = '2021-03-31' 
	AND s.fund_code IN (
SELECT
	t1.fund_code 
FROM
	t_fund_archives_stock t1 
WHERE
	t1.up_until_date = '2021-03-31' 
GROUP BY
	t1.fund_code 
HAVING
	sum( t1.net_worth_ratio ) > 55 
	)
```

```postgresql
-- 统计机构调用的次数，机构总数，按机构总数和调用次数排序

SELECT
	r.s_code,
	r.s_name,
	sum( r.org_sum ) org_total,
	count( * ) research_count 
FROM
	t_institution_research r 
WHERE
	r.start_date BETWEEN '2021-04-25' 
	AND '2021-05-01' 
	and r.s_code not like '68%'
GROUP BY
	r.s_code,
	r.s_name 
ORDER BY
	org_total DESC,
	research_count DESC LIMIT 30;

```