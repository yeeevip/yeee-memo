<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${tableClass.shortClassName}${mapperSuffix}">

    <sql id="searchBy">
        <trim prefix="where" prefixOverrides="and|or">
            <#if tableClass.allFields??>
            <#list tableClass.allFields as field>
            <if test="${field.fieldName} != null and ${field.fieldName} != ''">
                and ${field.columnName} = <#noparse>#{</#noparse>${field.fieldName}<#noparse>}</#noparse>
            </if>
            </#list>
            </#if>
        </trim>
    </sql>

    <select id="getList" resultType="${modelPackage}.${tableClass.shortClassName}">
        SELECT
        <#if tableClass.allFields??>
            <#list tableClass.allFields as field>
            ${field.columnName}<#if field_has_next>,</#if>
            </#list>
        </#if>
        FROM
            ${tableClass.tableName}
        <include refid="searchBy"/>
    </select>

    <select id="getOne" resultType="${modelPackage}.${tableClass.shortClassName}">
        SELECT
        <#if tableClass.allFields??>
            <#list tableClass.allFields as field>
            ${field.columnName}<#if field_has_next>,</#if>
            </#list>
        </#if>
        FROM
            ${tableClass.tableName}
        <include refid="searchBy"/>
        LIMIT 1
    </select>


    <resultMap type="${modelPackage}.${tableClass.shortClassName}" id="XXXXXXXXXXXXXXXDtoMap">
        <#if tableClass.allFields??>
            <#list tableClass.allFields as field>
        <result column="${tableClass.tableName}__${field.columnName}" property="${tableClass.variableName}.${field.columnName}" />
            </#list>
        </#if>
    </resultMap>


    <select id="xxxxxxxxxxxxxxxxxxxxxx" resultMap="XXXXXXXXXXXXXXXDtoMap">
        SELECT
        <#if tableClass.allFields??>
            <#list tableClass.allFields as field>
            ${tableClass.tableName}.${field.columnName} as ${tableClass.tableName}__${field.columnName}<#if field_has_next>,</#if>
            </#list>
        </#if>
        FROM
            ${tableClass.tableName}
    </select>

    <insert id="insert" parameterType="${modelPackage}.${tableClass.shortClassName}" useGeneratedKeys="true" keyProperty="id">
        INSERT INTO ${tableClass.tableName}
        <trim prefix="(" suffix=")" suffixOverrides=",">
        <#if tableClass.baseFields??>
            <#list tableClass.allFields as field>
            <if test="${field.fieldName} != null and ${field.fieldName} != '' ">
                ${field.columnName}<#if field_has_next>,</#if>
            </if>
            </#list>
        </#if>
        </trim>
        VALUE
        <trim prefix="(" suffix=")" suffixOverrides=",">
        <#if tableClass.baseFields??>
            <#list tableClass.allFields as field>
            <if test="${field.fieldName} != null and ${field.fieldName} != ''">
                <#noparse>#{</#noparse>${field.fieldName}<#noparse>}</#noparse><#if field_has_next>,</#if>
            </if>
            </#list>
        </#if>
        </trim>
        <selectKey resultType="int" order="AFTER" keyProperty="id">
            SELECT LAST_INSERT_ID() AS id
        </selectKey>
    </insert>

    <!-- 删掉if id -->
    <update id="updateByPrimaryKey" parameterType="${modelPackage}.${tableClass.shortClassName}">
        UPDATE ${tableClass.tableName}
        <trim prefix="set" suffixOverrides=",">
            <#if tableClass.baseFields??>
                <#list tableClass.allFields as field>
            <if test="${field.fieldName} != null and ${field.fieldName} != ''">
                ${field.columnName} = <#noparse>#{</#noparse>${field.fieldName}<#noparse>}</#noparse><#if field_has_next>,</#if>
            </if>
                </#list>
            </#if>
        </trim>
        WHERE id=<#noparse>#{id}</#noparse>
    </update>

    <!-- 批量插入  MyBatis在3.3.x修复了这个问题>自增 -->
    <insert id="batchInsert" parameterType="java.util.List">
        INSERT INTO ${tableClass.tableName}
        <trim prefix="(" suffix=")" suffixOverrides=",">
        <#if tableClass.allFields??>
            <#list tableClass.allFields as field>${field.columnName},</#list>
        </#if>
        </trim>
        VALUES
        <foreach collection="list" item="item" index="index" separator=",">
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <#if tableClass.allFields??>
            <#list tableClass.allFields as field><#noparse>#{</#noparse>item.${field.fieldName}<#noparse>}</#noparse>,</#list>
            </#if>
        </trim>
        </foreach>
    </insert >

</mapper>
