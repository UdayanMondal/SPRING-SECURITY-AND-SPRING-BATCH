package com.example.CorporateEmployee.Configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.FaultTolerantStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.PathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.CorporateEmployee.Entity.Employer;
import com.example.CorporateEmployee.Processor.EmployerProcessor;
import com.example.CorporateEmployee.Repository.EmployerRepository;

import jakarta.mail.internet.ParseException;
import jakarta.servlet.http.HttpSession;

@Configuration

public class SpringBatchConfig {
	@Autowired
	private EmployerRepository employerRepository;
	
	private HttpSession session;
	@Bean
	@StepScope
	public FlatFileItemReader<Employer> itemReader(@Value("#{jobParameters[fullPathFileName]}") String pathToFIle)
			throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		try {
			FlatFileItemReader<Employer> itemReader = new FlatFileItemReader<>();
			itemReader.setResource(new FileSystemResource(pathToFIle));
			// to give a unique name to the itemreader bean set the name
			itemReader.setName("csvReader");
			// to skip the header of csv file
			itemReader.setLinesToSkip(1);
			itemReader.setLineMapper(lineMapper());
			itemReader.setRecordSeparatorPolicy(blankLines());
			itemReader.open(new ExecutionContext());
			itemReader.read(); 
			itemReader.close();
			return itemReader;
		}

		catch (Exception ex) {
       System.out.println(ex.getCause());
			throw new Exception();

		}

	}

	private LineMapper<Employer> lineMapper() {
		DefaultLineMapper<Employer> lineMapper = new DefaultLineMapper<>();

		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");
		lineTokenizer.setStrict(false);

		String[] tokens = new String[] { "name", "address", "email", "noOfEmployee", "employerType", "netWorth" };
		lineTokenizer.setNames(tokens);
		BeanWrapperFieldSetMapper<Employer> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(Employer.class);
		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);
		lineMapper.afterPropertiesSet();
		return lineMapper;

	}

	

	@Bean
	public EmployerProcessor processor() {
		return new EmployerProcessor();
	}

	@Bean
	public RepositoryItemWriter<Employer> writer() {
		RepositoryItemWriter<Employer> writer = new RepositoryItemWriter<>();
		writer.setRepository(employerRepository);
		writer.setMethodName("save");
		return writer;
	}

	@SuppressWarnings("unchecked")
	@Bean
	public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager,
			FlatFileItemReader<Employer> itemReader) {
		return ((FaultTolerantStepBuilder<Employer, Employer>) new StepBuilder("csv-step", jobRepository).<Employer, Employer>chunk(10, transactionManager)
				.reader(itemReader).processor(processor()).writer(writer()).faultTolerant().listener(skipListener()).listener(customJobListener())
				.listener(emptyFileCheckListener())).skipPolicy(skipPolicy()).build();
	}

	@Bean
	public Job runJob(JobRepository jobRepository, PlatformTransactionManager transactionManager,CustomJobListener customJobListener,
			FlatFileItemReader<Employer> itemReader) {
		return new JobBuilder("importEmployers", jobRepository)
				.flow(step1(jobRepository, transactionManager,itemReader)).end().build();
	}

	@Bean
	public TaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
		asyncTaskExecutor.setConcurrencyLimit(10);
		return asyncTaskExecutor;
	}

	@Bean
	public SkipListener skipListener() {
		return new StepSkipListener();
	}

	@Bean
	public SkipPolicy skipPolicy() {
		return new ExceptionSkipPolicy();
	}

	@Bean
	public ItemCountListener listener() {
		return new ItemCountListener();
	}

	@Bean
	public BlankLineRecordSeparatorPolicy blankLine() {
		return new BlankLineRecordSeparatorPolicy();
	}
	
	@Bean 
	public BlankLineRecordSeparatorPolicy blankLines()
	{
		return new BlankLineRecordSeparatorPolicy ();
	}
	@Bean
	public EmptyFileCheckListener <Employer> emptyFileCheckListener ()
	{
		return new EmptyFileCheckListener();
	}
	@Bean
	public CustomJobListener customJobListener ()
	{
		return new CustomJobListener(session);
	}
	
}