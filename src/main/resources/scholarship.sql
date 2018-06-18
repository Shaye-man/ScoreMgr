#sql("count")
SELECT
	GROUP_CONCAT(sc.sId) AS sIds,
	GROUP_CONCAT(sc.sName) AS sNames,
	sc.clazzName,
	sc.clazzId,
	GROUP_CONCAT(sc.totalScore) AS totalScores,
	COUNT(*) AS total
FROM
	(
		SELECT
			student.username AS sId,
			student.`name` AS sName,
			clazz.`name` AS clazzName,
			student.clazzid AS clazzId,
			SUM(score) AS totalScore
		FROM
			student,
			score,
			clazz
		WHERE
			score.sid = student.username
		AND student.clazzid = clazz.id
		GROUP BY
			student.username,
			student.clazzid
		ORDER BY
			student.clazzid,
			totalScore DESC
	) AS sc
GROUP BY
	sc.clazzName
ORDER BY
	sc.clazzId
#end

#sql("sort")
SELECT
	student.username AS sId,
	student.`name` AS sName,
	clazz.`name` AS clazzName,
	student.clazzid AS clazzId,
	SUM(score) AS totalScore
FROM
	student,
	score,
	clazz
WHERE
	score.sid = student.username
AND student.clazzid = clazz.id
GROUP BY
	student.username,
	student.clazzid
ORDER BY
	student.clazzid,
	totalScore DESC
#end


#sql("rank")
SELECT
	*
FROM
	scholarship
ORDER BY
	id
LIMIT 3
#end

#sql("countScore")
SELECT
	student.username AS sId,
	student.`name` AS sName,
	clazz.id AS clazzId,
	clazz.`name` AS clazzName,
	SUM(score) AS totalScore,
	FORMAT(AVG(score), 2) AS averageScore
FROM
	student,
	score,
	clazz
WHERE
	score.sid = student.username
AND student.clazzid = clazz.id
GROUP BY
	student.username,
	student.clazzid
ORDER BY
	student.clazzid,
	totalScore DESC
#end

#sql("totalScore")
SELECT
	clazz.`name` AS clazzName,
	SUM(score) AS totalScore,
	FORMAT(AVG(score),2) AS averageScore
FROM
	course,
	score,
	clazz
WHERE
	score.cid = course.id
AND course.clazzid = clazz.id
AND clazz.id = ?
GROUP BY
	clazz.`name`
ORDER BY
clazz.id
#end