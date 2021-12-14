package org.slams.server.common.utils;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class AwsS3Uploader {

	private final AmazonS3Client amazonS3Client;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	public String upload(String base64, String dirName) {
		if (base64 == null) {
			log.info("이미지 base64가 비어있습니다. null를 반환합니다.");
			return null;
		}

		// content type 파싱
		String[] split = base64.split(",");
		String contentType = split[0].substring(split[0].indexOf(":") + 1, split[0].indexOf(";"));

		// base64 -> byte
		byte[] decode = Base64.getMimeDecoder().decode(split[1]);

		// byte -> inputStream
		ByteArrayInputStream inputStream = new ByteArrayInputStream(decode);

		// 메타데이터 생성
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(decode.length);
		metadata.setContentType(contentType);

		// filename 지정
		String fileName = dirName + "/" + UUID.randomUUID();

		// s3 업로드
		amazonS3Client.putObject(
			new PutObjectRequest(bucket, fileName, inputStream, metadata)
				.withCannedAcl(CannedAccessControlList.PublicRead)
		);

		log.info("이미지가 S3에 정상적으로 업로드되었습니다.");
		return amazonS3Client.getUrl(bucket, fileName).toString();
	}

}
