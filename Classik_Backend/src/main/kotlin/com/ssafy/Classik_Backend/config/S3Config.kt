package com.ssafy.Classik_Backend.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import io.github.cdimascio.dotenv.Dotenv
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class S3Config {

    @Bean
    fun amazonS3(): AmazonS3 {
        val dotenv = Dotenv.load()
        val accessKey = dotenv["AWS_ACCESS_KEY_ID"] ?: throw IllegalArgumentException("AWS_ACCESS_KEY_ID is missing")
        val secretKey = dotenv["AWS_SECRET_ACCESS_KEY"] ?: throw IllegalArgumentException("AWS_SECRET_ACCESS_KEY is missing")
        val region = "ap-northeast-2"

        val credentials = BasicAWSCredentials(accessKey, secretKey)
        return AmazonS3ClientBuilder.standard()
            .withRegion(region)
            .withCredentials(AWSStaticCredentialsProvider(credentials))
            .build()
    }
}